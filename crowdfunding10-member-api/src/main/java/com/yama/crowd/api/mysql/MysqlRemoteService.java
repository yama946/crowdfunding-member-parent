package com.yama.crowd.api.mysql;

import com.yama.crowd.entity.po.MemberPO;
import com.yama.crowd.entity.vo.DetailProjectVO;
import com.yama.crowd.entity.vo.PortalTypeVO;
import com.yama.crowd.entity.vo.ProjectVO;
import com.yama.crowd.util.ResultUtil;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 调用mysql的模块的远程调用接口
 */
@FeignClient("crowd-mysql")
public interface MysqlRemoteService {
    @RequestMapping("/get/memberpo/by/loginacct/remote")
    ResultUtil<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginacct")
                                                                String loginacct);

    @RequestMapping("/save/member/remote")
    public ResultUtil<String> saveMember(@RequestBody MemberPO memberPO);

    @RequestMapping("/save/projectvo/remote")
    ResultUtil<String> saveProjectVORemote(@RequestBody ProjectVO projectVO, @RequestParam("memberId") Integer memberId);

    @RequestMapping("/get/type/remote")
    ResultUtil<List<PortalTypeVO>> getPortalTypeRemote();

    @RequestMapping("/get/project/detail/remote/{projectId}")
    public ResultUtil<DetailProjectVO> getDetailProjectVORemote(@PathVariable("projectId")
                                                                        Integer projectId);
}
