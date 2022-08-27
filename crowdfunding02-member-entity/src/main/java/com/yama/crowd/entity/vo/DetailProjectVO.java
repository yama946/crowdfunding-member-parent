package com.yama.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 详细项目信息，其中包含详细的回报信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailProjectVO {
    private Integer projectId;
    private String projectName;
    private String projectDesc;
    private Integer followerCount;
    private Integer status;
    private String statusText;
    private Integer day;
    private Integer money;
    private Integer supportMoney;
    private Integer percentage;
    private String deployDate;
    private Integer lastDay;
    private Integer supporterCount;
    private String headerPicturePath;
    private List<String> detailPicturePathList;
    private List<DetailReturnVO> detailReturnVOList;
}
