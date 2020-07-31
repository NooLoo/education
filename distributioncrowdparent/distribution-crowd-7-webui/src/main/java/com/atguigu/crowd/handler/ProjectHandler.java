package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.ProjectOperationRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * 众筹项目操作
 */
@Controller
public class ProjectHandler {

    @Autowired
    private ProjectOperationRemoteService projectOperationRemoteService;



}
