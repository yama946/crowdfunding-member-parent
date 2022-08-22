package com.yama.crowd.api.mysql;

import com.yama.crowd.entity.po.MemberPO;
import com.yama.crowd.util.ResultUtil;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 调用mysql的模块的远程调用接口
 */

@FeignClient("crowd-mysql")
public interface MysqlRemoteService {
    @RequestMapping("/get/memberpo/by/loginacct/remote")
    ResultUtil<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginacct")
                                                                String loginacct);
}
