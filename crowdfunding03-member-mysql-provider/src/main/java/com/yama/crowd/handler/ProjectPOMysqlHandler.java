package com.yama.crowd.handler;

import com.yama.crowd.entity.vo.ProjectVO;
import com.yama.crowd.service.ProjectPOMysqlService;
import com.yama.crowd.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    ResultUtil<String> saveProjectVORemote(@RequestBody ProjectVO projectVO, @RequestParam("memberId") Integer memberId){
        try {
            projectPOMysqlService.saveProjectInfo(projectVO,memberId);
            return ResultUtil.ok(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(e.getMessage());
        }
    }
}
