package com.yama.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortalTypeVO {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 类型名
     */
    private String name;

    /**
     * 类型标记说明
     */
    private String remark;

    /**
     * 项目VO集合
     */
    private List<PortalProjectVO> portalProjectVOList;
}
