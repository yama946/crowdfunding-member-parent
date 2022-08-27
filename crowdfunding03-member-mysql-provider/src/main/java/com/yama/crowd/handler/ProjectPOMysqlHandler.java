package com.yama.crowd.handler;

import com.yama.crowd.entity.vo.DetailProjectVO;
import com.yama.crowd.entity.vo.PortalTypeVO;
import com.yama.crowd.entity.vo.ProjectVO;
import com.yama.crowd.service.ProjectPOMysqlService;
import com.yama.crowd.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.util.List;

@RestController
public class ProjectPOMysqlHandler {
    @Autowired
    private ProjectPOMysqlService projectPOMysqlService;

    /**
     * 保存项目信息
     * @param projectVO
     * @param memberId
     * @return
     */
    @RequestMapping("/save/projectvo/remote")
    public ResultUtil<String> saveProjectVORemote(@RequestBody ProjectVO projectVO, @RequestParam("memberId") Integer memberId){
        try {
            projectPOMysqlService.saveProjectInfo(projectVO,memberId);
            return ResultUtil.ok(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(e.getMessage());
        }
    }

    @RequestMapping("/get/type/remote")
    public ResultUtil<List<PortalTypeVO>> getPortalTypeRemote(){

        try {
            List<PortalTypeVO> protalTypeList = projectPOMysqlService.getProtalTypeList();
            return ResultUtil.ok(protalTypeList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(null);
        }
    }

    @RequestMapping("/get/project/detail/remote/{projectId}")
    public ResultUtil<DetailProjectVO> getDetailProjectVORemote(@PathVariable("projectId")
                                                                          Integer projectId) {
        try {
            DetailProjectVO detailProjectVO = projectPOMysqlService.getDetailProjectVO(projectId);
            return ResultUtil.ok(detailProjectVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(null);
        }
    }
}
