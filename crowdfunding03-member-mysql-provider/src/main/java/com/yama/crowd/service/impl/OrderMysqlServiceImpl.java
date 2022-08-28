package com.yama.crowd.service.impl;

import com.netflix.discovery.converters.Auto;
import com.yama.crowd.entity.po.AddressPO;
import com.yama.crowd.entity.po.AddressPOExample;
import com.yama.crowd.entity.po.OrderPO;
import com.yama.crowd.entity.po.OrderProjectPO;
import com.yama.crowd.entity.vo.AddressVO;
import com.yama.crowd.entity.vo.OrderProjectVO;
import com.yama.crowd.entity.vo.OrderVO;
import com.yama.crowd.mapper.AddressPOMapper;
import com.yama.crowd.mapper.OrderPOMapper;
import com.yama.crowd.mapper.OrderProjectPOMapper;
import com.yama.crowd.service.OrderMysqlService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class OrderMysqlServiceImpl implements OrderMysqlService {
    @Autowired
    private OrderPOMapper orderPOMapper;

    @Autowired
    private AddressPOMapper addressPOMapper;

    @Autowired
    private OrderProjectPOMapper orderProjectPOMapper;


    @Override
    public OrderProjectVO getOrderProjectVo(Integer returnId) {
        try {
            OrderProjectVO orderProjectVO = orderProjectPOMapper.selectOrderProjectVO(returnId);
            return orderProjectVO;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("mysql查询OrderProjectVO异常");
        }
    }

    @Override
    public List<AddressVO> getAddressListByMemberId(Integer memberId) {
        AddressPOExample addressPOExample = new AddressPOExample();
        AddressPOExample.Criteria criteria = addressPOExample.createCriteria();
        criteria.andMemberIdEqualTo(memberId);
        List<AddressPO> addressPOS = addressPOMapper.selectByExample(addressPOExample);
        List<AddressVO> addressVOList = new ArrayList<>();
        for (AddressPO addressPO : addressPOS){
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(addressPO,addressVO);
            addressVOList.add(addressVO);
        }
        return addressVOList;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    @Override
    public void saveAddress(AddressVO addressVO) {
        AddressPO addressPO = new AddressPO();
        BeanUtils.copyProperties(addressVO,addressPO);
        addressPOMapper.insert(addressPO);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveOrder(OrderVO orderVO) {
        // 创建OrderPO对象
        OrderPO orderPO = new OrderPO();
        // 从传入的OrderVO给OrderPO赋值
        BeanUtils.copyProperties(orderVO,orderPO);
        // 将OrderPO存入数据库
        orderPOMapper.insert(orderPO);
        // 得到存入后自增产生的order id
        Integer orderId = orderPO.getId();
        // 得到orderProjectVO
        OrderProjectVO orderProjectVO = orderVO.getOrderProjectVO();
        // 创建OrderProjectPO对象
        OrderProjectPO orderProjectPO = new OrderProjectPO();
        // 赋值
        BeanUtils.copyProperties(orderProjectVO,orderProjectPO);
        // 给orderProjectPO设置orderId
        orderProjectPO.setOrderId(orderId);
        // 存入数据库
        orderProjectPOMapper.insert(orderProjectPO);
    }
}
