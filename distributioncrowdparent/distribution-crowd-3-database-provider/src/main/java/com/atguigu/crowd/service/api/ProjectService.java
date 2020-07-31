package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.ProjectVO;

public interface ProjectService {
    void saveProject(ProjectVO projectVO, String memberId);
}
