package com.atguigu.crowd.controller;

import com.atguigu.crowd.entity.ProjectVO;
import com.atguigu.crowd.entity.ResultEntity;
import com.atguigu.crowd.service.api.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    /**
     * 保存整个众筹项目的所有数据
     */
    @RequestMapping("/save/project/remote/{memberId}")
    ResultEntity<String> saveProjectRemote(@RequestBody ProjectVO projectVO,@PathVariable("memberId") String memberId){
        try {
            projectService.saveProject(projectVO,memberId);
            return ResultEntity.successNoData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

}
