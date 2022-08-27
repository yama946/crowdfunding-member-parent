package com.yama.crowd.service;

import com.yama.crowd.entity.vo.DetailProjectVO;
import com.yama.crowd.entity.vo.PortalTypeVO;
import com.yama.crowd.entity.vo.ProjectVO;

import java.util.List;

public interface ProjectPOMysqlService {
    void saveProjectInfo(ProjectVO projectVO, Integer memberId);

    List<PortalTypeVO> getProtalTypeList();

    DetailProjectVO getDetailProjectVO(Integer projectId);
}
