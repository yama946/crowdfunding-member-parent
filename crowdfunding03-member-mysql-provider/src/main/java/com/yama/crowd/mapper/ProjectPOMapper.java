package com.yama.crowd.mapper;

import com.yama.crowd.entity.po.ProjectPO;
import com.yama.crowd.entity.po.ProjectPOExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectPOMapper {
    int countByExample(ProjectPOExample example);

    int deleteByExample(ProjectPOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ProjectPO record);

    int insertSelective(ProjectPO record);

    List<ProjectPO> selectByExample(ProjectPOExample example);

    ProjectPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ProjectPO record, @Param("example") ProjectPOExample example);

    int updateByExample(@Param("record") ProjectPO record, @Param("example") ProjectPOExample example);

    int updateByPrimaryKeySelective(ProjectPO record);

    int updateByPrimaryKey(ProjectPO record);

    void insertProjectType(@Param("projectId") int projectId,@Param("typeIdList") List<Integer> typeIdList);

    void insertProjectTag(@Param("projectId") int projectId, @Param("tagIdList") List<Integer> tagIdList);

    void insertdetailPicturePathList(@Param("projectId") int projectId,
                                     @Param("detailPicturePathList") List<String> detailPicturePathList);

}