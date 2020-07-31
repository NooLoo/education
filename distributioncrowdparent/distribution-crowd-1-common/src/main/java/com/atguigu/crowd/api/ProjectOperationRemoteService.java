package com.atguigu.crowd.api;

import com.atguigu.crowd.entity.MemberConfirmInfoVO;
import com.atguigu.crowd.entity.ProjectVO;
import com.atguigu.crowd.entity.ResultEntity;
import com.atguigu.crowd.entity.ReturnVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient
public interface ProjectOperationRemoteService {

    /**
     * 保存众筹项目的所有信息
     */
    @RequestMapping("/project/manager/save/whole/project")
    public ResultEntity<String> saveWholeProject(
            @RequestParam("memberSignToken") String memberSignToken,
            @RequestParam("projectTempToken") String projectTempToken);

    /**
     *保存确认信息
     */
    @RequestMapping("/project/manager/save/confirm/information")
    public ResultEntity<String> saveConfirmInformation(@RequestBody MemberConfirmInfoVO memberConfirmInfoVO);

    /**
     * 保存回报信息
     */
    @RequestMapping("/project/manager/save/return/information")
    public ResultEntity<String> saveReturnInformation(@RequestBody ReturnVO returnVO);

    /**
     * 保存项目信息
     */
    @RequestMapping("/project/manager/save/project/information")
    public ResultEntity<String> saveProjectInformation(@RequestBody ProjectVO projectVOFront);

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
            @RequestParam("detailPicturePathList") List<String> detailPicturePathList);

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
            @RequestParam("headerPicturePath") String headerPicturePath);

    /**
     * 项目创建初始化
     * @param memberSignToken 登录令牌
     */
    @RequestMapping("/project/manager/init/creation")
    public ResultEntity<ProjectVO> initCreation(@RequestParam("memberSignToken") String memberSignToken);
}
