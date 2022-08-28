package com.yama.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderProjectVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 发起人
     */
    private String launchName;

    /**
     * 回报内容
     */
    private String returnContent;

    /**
     * 回报数量
     */
    private Integer returnCount;
    /**
     * 支持单价
     */
    private Integer supportPrice;
    /**
     * 配送费用
     */
    private Integer freight;
    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 是否限购
     */
    private Integer signalPurchase;

    /**
     * 限购数额
     */
    private Integer purchase;
}
