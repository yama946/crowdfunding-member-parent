package com.yama.crowd.service.impl;

import com.yama.crowd.entity.po.MemberPO;
import com.yama.crowd.entity.po.MemberPOExample;
import com.yama.crowd.entity.po.MemberPOExample.*;
import com.yama.crowd.mapper.MemberPOMapper;
import com.yama.crowd.service.MemberMysqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
//配置事务支持
@Transactional(readOnly = true)
@Service
public class MemberMysqlServiceImpl implements MemberMysqlService {
    @Autowired
    private MemberPOMapper memberPOMapper;

    @Override
    public MemberPO getMemberPOByLoginAcct(String loginacct) throws NullPointerException {
        MemberPOExample memberPOExample = new MemberPOExample();
        Criteria criteria = memberPOExample.createCriteria();

        criteria.andLoginacctEqualTo(loginacct);
        List<MemberPO> list = memberPOMapper.selectByExample(memberPOExample);
        return list.get(0);
    }
}
