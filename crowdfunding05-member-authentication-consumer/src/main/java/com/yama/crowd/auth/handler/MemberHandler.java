package com.yama.crowd.auth.handler;

import com.yama.crowd.api.mysql.MysqlRemoteService;
import com.yama.crowd.api.redis.RedisRemoteService;
import com.yama.crowd.auth.config.ShortMessageParamConfig;
import com.yama.crowd.constant.CrowdConstantSon;
import com.yama.crowd.entity.po.MemberPO;
import com.yama.crowd.entity.vo.MemberVO;
import com.yama.crowd.util.CrowdMemberUtil;
import com.yama.crowd.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
public class MemberHandler {
    @Autowired
    private ShortMessageParamConfig shortMessageParamConfig;

    @Autowired
    private MysqlRemoteService mysqlRemoteService;

    @Autowired
    private RedisRemoteService redisRemoteService;

    /**
     * 发送验证码，并将验证码保存到redis中，用于下一步注册校验
     * @return
     */
    @ResponseBody
    @RequestMapping("/auth/member/send/short/message")
    public ResultUtil<String> getVerifyCode(@RequestBody @RequestParam("phoneNum") String phoneNum){
        ResultUtil<HttpResponse> sendMessageResult =null;
        //1.生成验证码
        String code = "";
        for (int i = 0; i < 6; i++) {
            int key = (int) (Math.random() * 10);
            code += key;
        }
        String verityCode="**code**:"+code+",**minute**:30";
        //2.发送验证码
        try{
            sendMessageResult  = CrowdMemberUtil.sendCodeByShortMessage(
                    shortMessageParamConfig.getHost(), shortMessageParamConfig.getPath(), shortMessageParamConfig.getPath(),
                    shortMessageParamConfig.getAppcode(), phoneNum, verityCode, shortMessageParamConfig.getSmsSignId(),
                    shortMessageParamConfig.getTemplateId()
            );
            //3.判断是否发送成功，并保存到redis中
            if (sendMessageResult.isResult()) {
                //将验证码保存到redis中
                ResultUtil<String> verityCodeKey = redisRemoteService.setRedisKeyValueRemoteWithTimeout(
                        CrowdConstantSon.REDIS_CODE_PREFIX+phoneNum, code, 30L, TimeUnit.MINUTES);
                if (verityCodeKey.isResult()) {
                    log.debug("验证码保存成功");
                }
                return ResultUtil.ok(null);
            }
            if (!sendMessageResult.isResult()) {
                return ResultUtil.fail("发送异常，请稍后重试");
            }
        }catch (Exception e){
            e.printStackTrace();
            log.debug("发送短信异常");
        }
        return null;
    }

    /**
     * 注册按扭，提交的数据进行处理，
     * 验证验证码，验证用户名是否唯一
     * 验证成功，删除验证码，并定向到登陆页面，
     * 验证失败，重新定向到注册页面再次注册
     * @param mv
     * @param memberVO
     * @return
     */
    @RequestMapping("/auth/do/member/register")
    public ModelAndView register(ModelAndView mv, MemberVO memberVO){
        //1.获取用户手机号，拼接验证码键值，
        String phoneNum = memberVO.getPhoneNum();
        String verityCodeKey = CrowdConstantSon.REDIS_CODE_PREFIX+phoneNum;
        //2.获取redis中验证码并进行校验
        ResultUtil<String> result = redisRemoteService.getRedisStringValueByKeyRemote(verityCodeKey);
        boolean rs = result.isResult();
        //判断返回是否有效
        if (!rs){
            mv.addObject("verityCodeMessage","手机号有误或者验证码失效，请检查重试");
            mv.setViewName("member-reg");
        }
        if (rs){
            String verityCodeData = result.getData();
            if (verityCodeData!=null){
                //3.判断两者验证码是否一致
                if (Objects.equals(verityCodeData,memberVO.getVerityCode())){
                    //删除当前验证码，并保存数据
                    redisRemoteService.removeRedisKeyRemote(verityCodeKey);
                    //密码加密，将用户数据保存到数据库
                    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    String source = memberVO.getUserpswd();
                    String target = passwordEncoder.encode(source);
                    memberVO.setUserpswd(target);
                    //将membervo转换成memberpo，使用set设置值
                    MemberPO memberPO = new MemberPO();
                    BeanUtils.copyProperties(memberVO,memberPO);
                    //执行保存
                    ResultUtil<String> resultEntity = mysqlRemoteService.saveMember(memberPO);

                    //如果，用户名不唯一会保存异常，
                    if(!resultEntity.isResult()){
                        mv.addObject("");
                        mv.setViewName("member-reg");
                    }
                    mv.setViewName("member-login");
                }
            }
        }
        return mv;
    }

    /**
     * 验证登陆操作
     * @param mv
     * @param loginacct
     * @param userpswd
     * @param session
     * @return
     */
    @RequestMapping("/auth/member/do/login")
    public String login(ModelAndView mv, @RequestParam("loginacct") String loginacct,
                        @RequestParam("userpswd") String userpswd, HttpSession session){
        //远程调用mysql方法，获取用户数据
        ResultUtil<MemberPO> result = mysqlRemoteService.getMemberPOByLoginAcctRemote(loginacct);
        MemberPO resultEntity = result.getData();
        //判断是否存在用户
/*        if (ResultEntity.FAILED.equals(resultEntity.getResult())){
            mv.addObject(CrowdConstantSon.ACCT_NAME_MESSAGE,resultEntity.getMessage());
            return "member-login";
//            mv.setViewName("member-login");
//            return mv;
        }*/
        if (resultEntity==null){
            mv.addObject(CrowdConstantSon.ACCT_NAME_MESSAGE,CrowdConstantSon.MESSAGE_LOGIN_FAILED);
            return "member-login";
//            mv.setViewName("member-login");
//            return mv;
        }
        //比较密码
        MemberPO user =resultEntity;
        String pswdDatabase = user.getUserpswd();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean matches = passwordEncoder.matches(userpswd, pswdDatabase);
        if (!matches){
            mv.addObject(CrowdConstantSon.ACCT_NAME_MESSAGE,CrowdConstantSon.MESSAGE_LOGIN_FAILED);
            return "member-login";
//            mv.setViewName("member-login");
//            return mv;
        }
        //将用户数据存放到session中
        MemberVO memberVO = new MemberVO(user.getId(), loginacct, userpswd,user.getUsername(),user.getEmail());
        session.setAttribute(CrowdConstantSon.ACCT_NAME_LOGIN_MEMEBER,memberVO);
        //此处应该重定向到会员中心页面
        return "redirect:/auth/member/to/center/page";

//        return mv;
    }

    /**
     * 退出登陆
     * @param session
     * @return
     */
    @RequestMapping("/auth/member/logout")
    public String logout(HttpSession session){
        //session强制失效
        session.invalidate();
        //重定向
        return "redirect:/";
    }

}
