package com.yama.crowd.order.handler;

import com.yama.crowd.api.mysql.MysqlRemoteService;
import com.yama.crowd.constant.CrowdConstant;
import com.yama.crowd.constant.CrowdConstantSon;
import com.yama.crowd.entity.vo.AddressVO;
import com.yama.crowd.entity.vo.MemberVO;
import com.yama.crowd.entity.vo.OrderProjectVO;
import com.yama.crowd.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Slf4j
public class OrderConsumerHandler {

    @Autowired
    private MysqlRemoteService mysqlRemoteService;

    @RequestMapping("/confirm/return/info/{returnId}")
    public String ConfirmReturnInfo(@PathVariable("returnId")Integer returnId, HttpSession session){
        //1.远程调用接口获取回报信息
        ResultUtil<OrderProjectVO> projectVOResult = mysqlRemoteService.getOrderProjectVoRemote(returnId);

        if (projectVOResult.isResult()){
            OrderProjectVO orderProjectVO = projectVOResult.getData();
            session.setAttribute("orderProjectVO",orderProjectVO);
        }
        //2.将回报信息放置到session中并返回页面
        return "confirm_return";
    }

    /**
     * 订单信息
     * @param returnCount
     * @return
     */
    @RequestMapping("/confirm/order/{returnCount}")
    public String showConfirmOrderInfo(@PathVariable("returnCount") Integer returnCount,HttpSession session){
        //1.将获取到的returncount数据放置到session中
        OrderProjectVO orderProjectVO = (OrderProjectVO)session.getAttribute("orderProjectVO");
        orderProjectVO.setReturnCount(returnCount);
        //2.redis再次设置session更新数据
        session.setAttribute("orderProjectVO",orderProjectVO);
        //3.获取当前用户的id
        MemberVO memberVO = (MemberVO) session.getAttribute(CrowdConstantSon.ATTR_NAME_LOGIN_MEMBER);
        log.debug("当前用户的数据：{}",memberVO);
        Integer memberId = memberVO.getId();
        //4.根据id查询地址列表集合数据
        ResultUtil<List<AddressVO>> addressList = mysqlRemoteService.getAddressListByMemberIdRemote (memberId);

        if (addressList.isResult()){
            List<AddressVO> data = addressList.getData();
            session.setAttribute("addressVOList",data);
        }
        return "order_confirm";
    }

    @RequestMapping("/save/address")
    public String saveAddressInfo(AddressVO addressVO,HttpSession session){
        mysqlRemoteService.saveAddressRemote(addressVO);
        //获取returncount，用来为重定向做准备
        OrderProjectVO orderProjectVO = (OrderProjectVO)session.getAttribute("orderProjectVO");
        Integer returnCount = orderProjectVO.getReturnCount();
        return "redirect:/order/confirm/order/"+returnCount;
    }
}
