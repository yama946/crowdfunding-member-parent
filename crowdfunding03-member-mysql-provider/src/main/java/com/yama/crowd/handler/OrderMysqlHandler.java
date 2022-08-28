package com.yama.crowd.handler;

import com.yama.crowd.entity.vo.AddressVO;
import com.yama.crowd.entity.vo.OrderProjectVO;
import com.yama.crowd.entity.vo.OrderVO;
import com.yama.crowd.service.OrderMysqlService;
import com.yama.crowd.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderMysqlHandler {
    @Autowired
    private OrderMysqlService orderMysqlService;

    /**
     * 获取OrderProjectVo信息
     * @param returnId
     * @return
     */
    @RequestMapping("/get/order/return/vo/remote")
    ResultUtil<OrderProjectVO> getOrderProjectVoRemote(@RequestParam("returnId") Integer returnId){
        try {
            OrderProjectVO orderProjectVO = orderMysqlService.getOrderProjectVo(returnId);
            return ResultUtil.ok(orderProjectVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(null);
        }
    }

    /**
     * 获取address数据
     * @param memberId
     * @return
     */
    @RequestMapping("get/address/list/by/memeber/id/remote")
    ResultUtil<List<AddressVO>> getAddressListByMemberIdRemote(@RequestParam("memberId") Integer memberId){
        try {
            List<AddressVO> addressVOList = orderMysqlService.getAddressListByMemberId(memberId);
            return ResultUtil.ok(addressVOList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(null);
        }
    }

    /**
     * 保存地址信息
     * @param addressVO
     */
    @RequestMapping("/save/address/remote")
    void saveAddressRemote(@RequestBody AddressVO addressVO){
        orderMysqlService.saveAddress(addressVO);
    }

    @RequestMapping("/save/order/remote")
    ResultUtil<String> saveOrderRemote(@RequestBody OrderVO orderVO){
        try {
            orderMysqlService.saveOrder(orderVO);
            return ResultUtil.ok("保存Order成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(e.getMessage());
        }
    }
}
