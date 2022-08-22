package com.yama.crowd.service;

import com.yama.crowd.entity.po.MemberPO;

public interface MemberMysqlService {
    /**
     * 根据账号获取用户
     * @param loginacct
     * @return
     */
    MemberPO getMemberPOByLoginAcct(String loginacct);
}
