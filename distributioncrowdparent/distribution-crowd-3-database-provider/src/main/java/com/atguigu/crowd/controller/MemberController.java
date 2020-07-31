package com.atguigu.crowd.controller;

import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.ResultEntity;
import com.atguigu.crowd.service.api.MemberService;
import com.atguigu.crowd.util.CrowdConstant;
import com.atguigu.crowd.util.CrowdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    @Autowired
    private MemberService memberService;


    /**
     * 查看登录信息
     * @param loginAcct  登录账号
     */
    @RequestMapping("/retrieve/loign/acct/count")
    public ResultEntity<Integer> retrieveLoignAcctCount(@RequestParam("loginAcct") String loginAcct) {
        if(!CrowdUtils.strEffectiveCheck(loginAcct)){
            return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        try {
            Integer count = memberService.getLoginAcctCount(loginAcct);
            return ResultEntity.successWithData(count);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }


    /**
     * 保存注册信息
     * @param memberPO  注册信息
     */
    @RequestMapping("/save/member/remote")
    public ResultEntity<String> saveMemberRemote(@RequestBody MemberPO memberPO){

        try {
            memberService.saveMemberPO(memberPO);
            return ResultEntity.successNoData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }


    /**
     *获得登录账号信息
     */
    @RequestMapping("/retrieve/member/by/login/acct")
    public ResultEntity<MemberPO> retrieveMemberByLoginAcct(@RequestParam("loginAcct") String loginAcct){

        try {
            MemberPO memberPO = memberService.getMemberbyLoginAcct(loginAcct);
            return ResultEntity.successWithData(memberPO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

}
