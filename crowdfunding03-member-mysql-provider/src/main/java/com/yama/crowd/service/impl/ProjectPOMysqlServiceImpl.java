package com.yama.crowd.service.impl;

import com.netflix.discovery.converters.Auto;
import com.yama.crowd.entity.po.MemberConfirmInfoPO;
import com.yama.crowd.entity.po.MemberLaunchInfoPO;
import com.yama.crowd.entity.po.ProjectPO;
import com.yama.crowd.entity.po.ReturnPO;
import com.yama.crowd.entity.vo.*;
import com.yama.crowd.mapper.MemberConfirmInfoPOMapper;
import com.yama.crowd.mapper.MemberLaunchInfoPOMapper;
import com.yama.crowd.mapper.ProjectPOMapper;
import com.yama.crowd.mapper.ReturnPOMapper;
import com.yama.crowd.service.ProjectPOMysqlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional(readOnly = true)
@Service
@Slf4j
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

    /**
     * 获取类型和相关项目
     * @return
     */
    @Override
    public List<PortalTypeVO> getProtalTypeList() {
        List<PortalTypeVO> portalTypeVOList = projectPOMapper.selectPortalTypeVOList();
        return portalTypeVOList;
    }

    @Override
    public DetailProjectVO getDetailProjectVO(Integer projectId) {
        // 1.查询得到 DetailProjectVO 对象
        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(projectId);
        log.debug("项目详细信息：{}",detailProjectVO);
        // 2.根据 status 确定 statusText
        Integer status = detailProjectVO.getStatus();
        switch (status) {
            case 0:
                detailProjectVO.setStatusText("审核中");
                break;
            case 1:
                detailProjectVO.setStatusText("众筹中");
                break;
            case 2:
                detailProjectVO.setStatusText("众筹成功");
                break;
            case 3:
                detailProjectVO.setStatusText("已关闭");
                break;
            default:
                break;
        }
        // 3.根据 deployeDate 计算 lastDay
        // 2020-10-15
        String deployDate = detailProjectVO.getDeployDate();
        // 获取当前日期
        Date currentDay = new Date();
        // 把众筹日期解析成 Date 类型
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date deployDay = format.parse(deployDate);
            // 获取当前当前日期的时间戳
            long currentTimeStamp = currentDay.getTime();
            // 获取众筹日期的时间戳
            long deployTimeStamp = deployDay.getTime();
            // 两个时间戳相减计算当前已经过去的时间
            long pastDays = (currentTimeStamp - deployTimeStamp) / 1000 / 60 / 60 / 24;
            // 获取总的众筹天数
            Integer totalDays = detailProjectVO.getDay();
            // 使用总的众筹天数减去已经过去的天数得到剩余天数
            Integer lastDay = (int) (totalDays - pastDays);
            detailProjectVO.setLastDay(lastDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return detailProjectVO;
    }
}
