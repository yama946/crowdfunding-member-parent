package com.yama.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortalProjectVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *项目id
     */
    private Integer projectId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 项目头图
     */
    private String headerPicturePath;
    /**
     * 计划筹集金额
     */
    private Integer money;
    /**
     * 项目截至日期
     */
    private String deployDate;
    /**
     * 项目进度百分比
     */
    private Integer percentage;
    /**
     * 项目支持者
     */
    private Integer supporter;


}
