package com.yama.crowd.service.impl;

import com.netflix.discovery.converters.Auto;
import com.yama.crowd.entity.po.MemberConfirmInfoPO;
import com.yama.crowd.entity.po.MemberLaunchInfoPO;
import com.yama.crowd.entity.po.ProjectPO;
import com.yama.crowd.entity.po.ReturnPO;
import com.yama.crowd.entity.vo.MemberConfirmInfoVO;
import com.yama.crowd.entity.vo.MemberLauchInfoVO;
import com.yama.crowd.entity.vo.ProjectVO;
import com.yama.crowd.entity.vo.ReturnVO;
import com.yama.crowd.mapper.MemberConfirmInfoPOMapper;
import com.yama.crowd.mapper.MemberLaunchInfoPOMapper;
import com.yama.crowd.mapper.ProjectPOMapper;
import com.yama.crowd.mapper.ReturnPOMapper;
import com.yama.crowd.service.ProjectPOMysqlService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class ProjectPOMysqlServiceImpl implements ProjectPOMysqlService {
    @Autowired
    private ProjectPOMapper projectPOMapper;

    @Autowired
    private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;

    @Autowired
    private ReturnPOMapper returnPOMapper;

    @Autowired
    private MemberConfirmInfoPOMapper memberConfirmInfoPOMapper;

    /**
     * 保存项目信息
     * @param projectVO
     * @param memberId
     */
    @Transactional(readOnly = false,propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    @Override
    public void saveProjectInfo(ProjectVO projectVO, Integer memberId) {
        //1.将项目信息保存到ProjectPO中并获取protjecid
        ProjectPO projectPO = new ProjectPO();
        BeanUtils.copyProperties(projectVO,projectPO);
        //2.设置projectPO
        projectPO.setMemberid(memberId);
        projectPO.setStatus(0);
        projectPO.setCreatedate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        //3.添加到数据库中
        projectPOMapper.insertSelective(projectPO);
        int projectId = projectPO.getId();
        //4.将数据存放到t_project_type表中。
        projectPOMapper.insertProjectType(projectId,projectVO.getTypeIdList());
        //5.将数据保存到t_project_tag表中
        projectPOMapper.insertProjectTag(projectId,projectVO.getTagIdList());
        //6.保存项目信息图片地址信息
        projectPOMapper.insertdetailPicturePathList(projectId,projectVO.getDetailPicturePathList());
        //7.保存发起人信息
        MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();
        MemberLauchInfoVO memberLauchInfoVO = projectVO.getMemberLauchInfoVO();
        BeanUtils.copyProperties(memberLauchInfoVO,memberLaunchInfoPO);
        memberLaunchInfoPO.setMemberid(memberId);
        memberLaunchInfoPOMapper.insert(memberLaunchInfoPO);
        //8.保存回报信息
        List<ReturnPO> returnPOList = new ArrayList<>();
        for (ReturnVO returnVO : projectVO.getReturnVOList()){
            ReturnPO returnPO = new ReturnPO();
            BeanUtils.copyProperties(returnVO,returnPO);
            returnPOList.add(returnPO);
        }
        returnPOMapper.insertReturnPOList(projectId,returnPOList);
        //9.保存确认信息
        MemberConfirmInfoVO memberConfirmInfoVO = projectVO.getMemberConfirmInfoVO();
        MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO();
        BeanUtils.copyProperties(memberConfirmInfoVO,memberConfirmInfoPO);
        memberConfirmInfoPO.setMemberid(memberId);
        memberConfirmInfoPOMapper.insert(memberConfirmInfoPO);
    }
}
