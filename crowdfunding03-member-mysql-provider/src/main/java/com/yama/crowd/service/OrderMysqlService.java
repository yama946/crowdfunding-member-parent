package com.yama.crowd.service;

import com.yama.crowd.entity.vo.AddressVO;
import com.yama.crowd.entity.vo.OrderProjectVO;
import com.yama.crowd.entity.vo.OrderVO;

import java.util.List;

public interface OrderMysqlService {
    OrderProjectVO getOrderProjectVo(Integer returnId);

    List<AddressVO> getAddressListByMemberId(Integer memberId);

    void saveAddress(AddressVO addressVO);

    void saveOrder(OrderVO orderVO);
}
