package com.yama.crowd.handler;

import com.yama.crowd.constant.CrowdConstant;
import com.yama.crowd.entity.po.MemberPO;
import com.yama.crowd.service.MemberMysqlService;
import com.yama.crowd.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberMysqlHandler {
    @Autowired
    private MemberMysqlService memberMysqlService;

    /**
     * 通过登陆账户获取用户
     * @param loginacct
     * @return
     */
    @RequestMapping("/get/memberpo/by/loginacct/remote")
    ResultUtil<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginacct") String loginacct){
        try{
            MemberPO memberPO = memberMysqlService.getMemberPOByLoginAcct(loginacct);
            return ResultUtil.ok(memberPO);
        }catch (Exception e){
            //TODO 这里暂时不使用异常处理器，而是扑捉到异常直接返回
            e.printStackTrace();
            return ResultUtil.fail();
        }
    }

    /**
     * 将注册提交的用户数据进行保存
     * @param memberPO
     * @return
     */
    @RequestMapping("/save/member/remote")
    public ResultUtil<String> saveMember(@RequestBody MemberPO memberPO){
        try{
            memberMysqlService.saveMember(memberPO);
            return ResultUtil.ok(null);
        }catch (Exception e){
            if (e instanceof DuplicateKeyException){
                return ResultUtil.fail(CrowdConstant.MESSAGE_LOGIN_ACCt_ALREADY_IN_USE);
            }
            return ResultUtil.fail(e.getMessage());
        }
    }



}
