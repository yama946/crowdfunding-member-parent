package com.yama.crowd.service;

import com.yama.crowd.entity.vo.ProjectVO;

public interface ProjectPOMysqlService {
    void saveProjectInfo(ProjectVO projectVO, Integer memberId);
}
