package com.yama.crowd.auth.handler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProtalHandler {
    @RequestMapping("/")
    public ModelAndView showPortalPage(ModelAndView modelAndView) {
        // 这里实际开发中需要加载数据……
        modelAndView.setViewName("protal");
        return modelAndView;
    }
}
