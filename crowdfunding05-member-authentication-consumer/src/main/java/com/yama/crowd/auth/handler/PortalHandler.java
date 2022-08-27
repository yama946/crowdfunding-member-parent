package com.yama.crowd.auth.handler;

import com.yama.crowd.api.mysql.MysqlRemoteService;
import com.yama.crowd.constant.CrowdConstant;
import com.yama.crowd.entity.vo.PortalTypeVO;
import com.yama.crowd.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class PortalHandler {

    @Autowired
    private MysqlRemoteService mysqlRemoteService;

    @RequestMapping("/")
    public String showPortalPage(ModelMap modelMap) {
        ResultUtil<List<PortalTypeVO>> portalTypeRemote = mysqlRemoteService.getPortalTypeRemote();
        if (portalTypeRemote.isResult()){
            List<PortalTypeVO> portalTypeVOList = portalTypeRemote.getData();
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_TYPE_LIST,portalTypeVOList);
        }
        return "portal";
    }


}
