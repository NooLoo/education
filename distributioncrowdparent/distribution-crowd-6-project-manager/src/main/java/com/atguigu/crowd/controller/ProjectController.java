package com.atguigu.crowd.controller;

import com.alibaba.fastjson.JSON;
import com.atguigu.crowd.api.DataBaseOperationRemoteService;
import com.atguigu.crowd.api.RedisOperationRemoteService;
import com.atguigu.crowd.entity.MemberConfirmInfoVO;
import com.atguigu.crowd.entity.ProjectVO;
import com.atguigu.crowd.entity.ResultEntity;
import com.atguigu.crowd.entity.ReturnVO;
import com.atguigu.crowd.util.CrowdConstant;
import com.atguigu.crowd.util.CrowdUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class ProjectController {

    @Autowired
    private DataBaseOperationRemoteService dataBaseOperationRemoteService;

    @Autowired
    private RedisOperationRemoteService redisOperationRemoteService;

    /**
     * 保存众筹项目的所有信息
     */
    @RequestMapping("/project/manager/save/whole/project")
    public ResultEntity<String> saveWholeProject(
            @RequestParam("memberSignToken") String memberSignToken,
            @RequestParam("projectTempToken") String projectTempToken){

        //1.检查是否登录
        ResultEntity<String> resultEntity = redisOperationRemoteService.retrieveStringValueByStringKey(memberSignToken);
        if(ResultEntity.FAILED.equals(resultEntity.getResult())){
            return ResultEntity.failed(resultEntity.getMessage());
        }

        String memberId = resultEntity.getData();

        ResultEntity<String> resultProKeyEntity = redisOperationRemoteService.retrieveStringValueByStringKey(projectTempToken);
        if(ResultEntity.FAILED.equals(resultProKeyEntity.getResult())){
            return ResultEntity.failed(resultProKeyEntity.getMessage());
        }

        String projectVOJSON = resultProKeyEntity.getData();

        ProjectVO projectVO = JSON.parseObject(projectVOJSON, ProjectVO.class);

        return dataBaseOperationRemoteService.saveProjectRemote(projectVO,memberId);
    }

    /**
     *保存确认信息
     */
    @RequestMapping("/project/manager/save/confirm/information")
    public ResultEntity<String> saveConfirmInformation(@RequestBody MemberConfirmInfoVO memberConfirmInfoVO){

        // 获取memberSignToken
        String memberSignToken = memberConfirmInfoVO.getMemberSignToken();

        //1.检查是否登录
        ResultEntity<String> resultEntity = redisOperationRemoteService.retrieveStringValueByStringKey(memberSignToken);
        if(ResultEntity.FAILED.equals(resultEntity.getResult())){
            return ResultEntity.failed(resultEntity.getMessage());
        }

        // 从projectVOFront中获取projectTempToken
        String projectTempToken = memberConfirmInfoVO.getProjectTempToken();

        ResultEntity<String> resultProKeyEntity = redisOperationRemoteService.retrieveStringValueByStringKey(projectTempToken);
        if(ResultEntity.FAILED.equals(resultProKeyEntity.getResult())){
            return ResultEntity.failed(resultProKeyEntity.getMessage());
        }


        String projectVOJSON = resultProKeyEntity.getData();
        ProjectVO projectVO = JSON.parseObject(projectVOJSON, ProjectVO.class);

        projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);

        String jsonString = JSON.toJSONString(projectVO);

        return redisOperationRemoteService.saveNormalStringKeyValue(projectTempToken,jsonString,-1);
    }

    /**
     * 保存回报信息
     */
    @RequestMapping("/project/manager/save/return/information")
    public ResultEntity<String> saveReturnInformation(@RequestBody ReturnVO returnVO){
        // 获取memberSignToken
        String memberSignToken = returnVO.getMemberSignToken();

        //1.检查是否登录
        ResultEntity<String> resultEntity = redisOperationRemoteService.retrieveStringValueByStringKey(memberSignToken);
        if(ResultEntity.FAILED.equals(resultEntity.getResult())){
            return ResultEntity.failed(resultEntity.getMessage());
        }

        // 从projectVOFront中获取projectTempToken
        String projectTempToken = returnVO.getProjectTempToken();

        ResultEntity<String> resultProKeyEntity = redisOperationRemoteService.retrieveStringValueByStringKey(projectTempToken);
        if(ResultEntity.FAILED.equals(resultProKeyEntity.getResult())){
            return ResultEntity.failed(resultProKeyEntity.getMessage());
        }


        String projectVOJSON = resultProKeyEntity.getData();
        ProjectVO projectVO = JSON.parseObject(projectVOJSON, ProjectVO.class);

        //获取旧的回报集合
        List<ReturnVO> returnVOList = projectVO.getReturnVOList();
        if(!CrowdUtils.collectionEffectiveCheck(returnVOList)){
            returnVOList = new ArrayList<ReturnVO>();

            projectVO.setReturnVOList(returnVOList);
        }

        returnVOList.add(returnVO);

        String jsonString = JSON.toJSONString(projectVO);

        return redisOperationRemoteService.saveNormalStringKeyValue(projectTempToken,jsonString,-1);
    }

    /**
     * 保存项目信息
     */
    @RequestMapping("/project/manager/save/project/information")
    public ResultEntity<String> saveProjectInformation(@RequestBody ProjectVO projectVOFront) {

        // 获取memberSignToken
        String memberSignToken = projectVOFront.getMemberSignToken();

        //1.检查是否登录
        ResultEntity<String> resultEntity = redisOperationRemoteService.retrieveStringValueByStringKey(memberSignToken);
        if(ResultEntity.FAILED.equals(resultEntity.getResult())){
            return ResultEntity.failed(resultEntity.getMessage());
        }

        // 从projectVOFront中获取projectTempToken
        String projectTempToken = projectVOFront.getProjectTempToken();

        ResultEntity<String> resultProKeyEntity = redisOperationRemoteService.retrieveStringValueByStringKey(projectTempToken);
        if(ResultEntity.FAILED.equals(resultProKeyEntity.getResult())){
            return ResultEntity.failed(resultProKeyEntity.getMessage());
        }


        String projectVOJSON = resultProKeyEntity.getData();
        ProjectVO projectVO = JSON.parseObject(projectVOJSON, ProjectVO.class);

        projectVOFront.setDetailPicturePathList(projectVO.getDetailPicturePathList());
        projectVOFront.setHeaderPicturePath(projectVO.getHeaderPicturePath());

        BeanUtils.copyProperties(projectVOFront,projectVO);

        String jsonString = JSON.toJSONString(projectVO);

        return redisOperationRemoteService.saveNormalStringKeyValue(projectTempToken,jsonString,-1);
    }

    /**
     * 保存图片详细信息
     * @param memberSignToken 登录令牌
     * @param projectTempToken 项目临时令牌
     * @param detailPicturePathList 图片详细信息
     */
    @RequestMapping("/project/manager/save/detail/picture/path/list")
    public ResultEntity<String> saveDetailPicturePathList(
            @RequestParam("memberSignToken") String memberSignToken,
            @RequestParam("projectTempToken") String projectTempToken,
            @RequestParam("detailPicturePathList") List<String> detailPicturePathList){

        //1.检查是否登录
        ResultEntity<String> resultEntity = redisOperationRemoteService.retrieveStringValueByStringKey(memberSignToken);
        if(ResultEntity.FAILED.equals(resultEntity.getResult())){
            return ResultEntity.failed(resultEntity.getMessage());
        }

        ResultEntity<String> resultProKeyEntity = redisOperationRemoteService.retrieveStringValueByStringKey(projectTempToken);
        if(ResultEntity.FAILED.equals(resultProKeyEntity.getResult())){
            return ResultEntity.failed(resultProKeyEntity.getMessage());
        }

        String projectVOJSON = resultProKeyEntity.getData();

        ProjectVO projectVO = JSON.parseObject(projectVOJSON, ProjectVO.class);
        projectVO.setDetailPicturePathList(detailPicturePathList);

        String jsonString = JSON.toJSONString(projectVO);

        return redisOperationRemoteService.saveNormalStringKeyValue(projectTempToken,jsonString,-1);
    }

    /**
     * 保存头图片的路径
     * @param memberSignToken 登录令牌
     * @param projectTempToken 项目临时令牌
     * @param headerPicturePath 头图片路径
     */
    @RequestMapping("/project/manager/save/head/picture/path")
    public ResultEntity<String> saveHeadPicPath(
            @RequestParam("memberSignToken") String memberSignToken,
            @RequestParam("projectTempToken") String projectTempToken,
            @RequestParam("headerPicturePath") String headerPicturePath){

        //1.检查是否登录
        ResultEntity<String> resultEntity = redisOperationRemoteService.retrieveStringValueByStringKey(memberSignToken);
        if(ResultEntity.FAILED.equals(resultEntity.getResult())){
            return ResultEntity.failed(resultEntity.getMessage());
        }

        ResultEntity<String> resultProKeyEntity = redisOperationRemoteService.retrieveStringValueByStringKey(projectTempToken);
        if(ResultEntity.FAILED.equals(resultProKeyEntity.getResult())){
            return ResultEntity.failed(resultProKeyEntity.getMessage());
        }

        String projectVOJSON = resultProKeyEntity.getData();

        ProjectVO projectVO = JSON.parseObject(projectVOJSON, ProjectVO.class);
        projectVO.setHeaderPicturePath(headerPicturePath);

        String jsonString = JSON.toJSONString(projectVO);

        return redisOperationRemoteService.saveNormalStringKeyValue(projectTempToken,jsonString,-1);
    }


    /**
     * 项目创建初始化
     * @param memberSignToken 登录令牌
     */
    @RequestMapping("/project/manager/init/creation")
    public ResultEntity<ProjectVO> initCreation(@RequestParam("memberSignToken") String memberSignToken){

        //1.检查是否登录
        ResultEntity<String> resultEntity = redisOperationRemoteService.retrieveStringValueByStringKey(memberSignToken);
        if(ResultEntity.FAILED.equals(resultEntity.getResult())){
            return ResultEntity.failed(resultEntity.getMessage());
        }

        String memberId = resultEntity.getData();

        if(memberId == null){
           return ResultEntity.failed(CrowdConstant.MESSAGE_ACCESS_DENIED);
        }
        //2.创建一个projectVO对象备用
        ProjectVO projectVO = new ProjectVO();

        //生成临时项目令牌
        String projectTempToken = CrowdConstant.REDIS_PROJECT_TEMP_TOKEN_PREFIX + (UUID.randomUUID().toString().replaceAll("-",""));
        projectVO.setProjectTempToken(projectTempToken);

        //3.将projectVO对象转换成json
        String s = JSON.toJSONString(projectVO);

        //4.将数据存到redis里面
        redisOperationRemoteService.saveNormalStringKeyValue(projectTempToken,s,-1);

        return ResultEntity.successWithData(projectVO);
    }
}
