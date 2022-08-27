package com.yama.crowd.test;

import com.netflix.discovery.converters.Auto;
import com.yama.crowd.CrowdMemberMysql;
import com.yama.crowd.entity.po.MemberPO;
import com.yama.crowd.entity.vo.PortalTypeVO;
import com.yama.crowd.mapper.MemberPOMapper;
import com.yama.crowd.mapper.ProjectPOMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CrowdMemberMysql.class})
@Slf4j
public class MybatisTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private ProjectPOMapper projectPOMapper;

    @Autowired
    private MemberPOMapper memberPOMapper;

    @Test
    public void getConnection(){
        try {
            Connection connection = dataSource.getConnection();
            log.debug("获取的连接为：{}",connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMapper(){
        BCryptPasswordEncoder passwordEncoder  = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode("123456");
        MemberPO memberPO = new MemberPO(null, "jack", password, "杰克",
                "yanmu@163.com", 1, 1, "汤姆", "", null);
        int insert = memberPOMapper.insertSelective(memberPO);
        System.out.println("影响的行数："+memberPO.getId());
    }

    @Test
    public void testGetPortalType(){
        List<PortalTypeVO> portalTypeVOS = projectPOMapper.selectPortalTypeVOList();
        for (PortalTypeVO portalTypeVO : portalTypeVOS){
            log.debug("标签名:{}",portalTypeVO.getName());
            log.debug("标签描述：{}",portalTypeVO.getRemark());
            log.info(portalTypeVO.getPortalProjectVOList().toString());
        }
    }
}
