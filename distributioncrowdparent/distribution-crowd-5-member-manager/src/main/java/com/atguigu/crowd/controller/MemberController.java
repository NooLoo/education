package com.atguigu.crowd.controller;

import com.atguigu.crowd.api.DataBaseOperationRemoteService;
import com.atguigu.crowd.api.RedisOperationRemoteService;
import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.MemberSignSuccessVO;
import com.atguigu.crowd.entity.MemberVO;
import com.atguigu.crowd.entity.ResultEntity;
import com.atguigu.crowd.util.CrowdConstant;
import com.atguigu.crowd.util.CrowdUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class MemberController {

    @Autowired
    private RedisOperationRemoteService redisRemoteService;

    @Autowired
    private DataBaseOperationRemoteService dataBaseOperationRemoteService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 登出功能
     */
    @RequestMapping("/member/logout")
    public ResultEntity<String> logout(@RequestParam("token") String token) {
        return redisRemoteService.removeByKey(token);
    }

    /**
     * 登录功能
     */
    @RequestMapping("/member/login")
    public ResultEntity<MemberSignSuccessVO> login(@RequestParam("loginacct") String loginacct, @RequestParam("userpswd") String userpswd){

        //1.根据账号查询数据库获取MemberPO对象
        ResultEntity<MemberPO> resultEntity = dataBaseOperationRemoteService.retrieveMemberByLoginAcct(loginacct);
        if(ResultEntity.FAILED.equals(resultEntity.getResult())){
            return ResultEntity.failed(resultEntity.getMessage());
        }

        MemberPO memberPO = resultEntity.getData();
        String userpswdDatabase = memberPO.getUserpswd();

        //2.将输入的密码与数据库中的比较
        boolean matches = passwordEncoder.matches(userpswd, userpswdDatabase);

        //3.判断密码是否正确，通过登录
        if(!matches){
            return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        //4.生成token
        String token = CrowdUtils.generateToken();
        String id = memberPO.getId() + "";

        //5.将生成的token保存到redis中（key:token码 value:用户id）
        ResultEntity<String> resultEntitySaveToken = redisRemoteService.saveNormalStringKeyValue(token, id, 30);
        if(ResultEntity.FAILED.equals(resultEntitySaveToken.getResult())){
            return ResultEntity.failed(resultEntitySaveToken.getMessage());
        }

        MemberSignSuccessVO memberSignSuccessVO = new MemberSignSuccessVO();

        BeanUtils.copyProperties(memberPO,memberSignSuccessVO);
        memberSignSuccessVO.setToken(token);

        return ResultEntity.successWithData(memberSignSuccessVO);
    }

    /**
     * 注册功能
     */
    @RequestMapping("/member/manage/register")
    public ResultEntity<String> register(@RequestBody MemberVO memberVO){

        //1.判断输入的验证码是否有效
        String randomCode = memberVO.getRandomCode();
        if(!CrowdUtils.strEffectiveCheck(randomCode)){
            return ResultEntity.failed(CrowdConstant.MESSAGE_RANDOM_CODE_LENGTH_INVALID);
        }

        //2.手机号有效性检测
        String phoneNum = memberVO.getPhoneNum();
        if(!CrowdUtils.strEffectiveCheck(phoneNum)){
            return ResultEntity.failed(CrowdConstant.MESSAGE_PHONE_NUM_INVALID);
        }


        //3.拼接Redis储存的验证码的key
        String randomCodeKey = CrowdConstant.REDIS_RANDOM_CODE_PREFIX + phoneNum;


        //4.远程调用redis-provider验证验证码
        ResultEntity<String> resultEntity = redisRemoteService.retrieveStringValueByStringKey(randomCodeKey);

        //判断验证码检验结果是否有结果
        if(ResultEntity.FAILED.equals(resultEntity.getResult())){
            return resultEntity;
        }

        //将从redis中取出的验证码赋值给randomCodeRedis变量
        String randomCodeRedis = resultEntity.getData();

        //5.判断redis中的验证码是否不存在（已过期）
        if (randomCodeRedis == null){
            return ResultEntity.failed(CrowdConstant.MESSAGE_CODE_NOT_EXISTS);
        }

        //6.判断输入的验证码与redis缓存的验证码是否一致
        if(!Objects.equals(randomCode,randomCodeRedis)){
            return ResultEntity.failed(CrowdConstant.MESSAGE_CODE_NOT_MATCH);
        }

        //7.如果一致则删除redis中的key
        ResultEntity<String> resultRemoveEntity = redisRemoteService.removeByKey(randomCodeKey);
        if(ResultEntity.FAILED.equals(resultRemoveEntity.getResult())){
            return resultRemoveEntity;
        }

        //8.远程调用database-provider查看账号是否被调用
        String loginacct = memberVO.getLoginacct();
        ResultEntity<Integer> retrieveResultEntity = dataBaseOperationRemoteService.retrieveLoignAcctCount(loginacct);

        if(ResultEntity.FAILED.equals(retrieveResultEntity.getResult())){
            return ResultEntity.failed(retrieveResultEntity.getMessage());
        }

        Integer count = retrieveResultEntity.getData();
        //9.判断用户名是否被占用
        if(count > 0){
            return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
        }

        //10.如果没有被占用对密码进行加密
        String userpswd = memberVO.getUserpswd();
        String userpwsdencode = passwordEncoder.encode(userpswd);
        memberVO.setUserpswd(userpwsdencode);

        //11.将注册信息保存进数据库
        MemberPO memberPO = new MemberPO();

        //调用BeanUtils工具类直接注入
        BeanUtils.copyProperties(memberVO,memberPO);
        return dataBaseOperationRemoteService.saveMemberRemote(memberPO);
    }


    /**
     * 发送手机验证码
     * @param phoneNum  所要发送的手机号
     */
    @RequestMapping("/member/manage/send/code")
    public ResultEntity<String> sendCode(@RequestParam("phoneNum") String phoneNum){

        if(!CrowdUtils.strEffectiveCheck(phoneNum)){
            return ResultEntity.failed(CrowdConstant.MESSAGE_PHONE_NUM_INVALID);
        }

        //生成验证码
        int length = 4;
        String randomNum = CrowdUtils.randomCode(length);

        //保存到redis
        Integer timeoutMinute = 15;
        String nomalKey = CrowdConstant.REDIS_RANDOM_CODE_PREFIX + phoneNum;

        ResultEntity<String> resultEntity = redisRemoteService.saveNormalStringKeyValue(nomalKey, randomNum, timeoutMinute);
        //判断保存是否失败
        if(ResultEntity.FAILED.equals(resultEntity.getResult())){
            return resultEntity;
        }
        //发送验证码到用户手机
        String appCode = "1090c626ea514269b8a250e5a01b6009";
        CrowdUtils.sendShortMessage(appCode,randomNum,phoneNum);

        return ResultEntity.successNoData();
    }

}
