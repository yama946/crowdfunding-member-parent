package com.yama.crowd.project.handler;

import com.yama.crowd.api.mysql.MysqlRemoteService;
import com.yama.crowd.constant.CrowdConstant;
import com.yama.crowd.constant.CrowdConstantSon;
import com.yama.crowd.entity.vo.*;
import com.yama.crowd.project.oss.OSSProperties;
import com.yama.crowd.project.oss.OSSUploadUtile;
import com.yama.crowd.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.xml.transform.Result;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class ProjectConsumerHandler {
    @Autowired
    private OSSProperties ossProperties;

    @Autowired
    private MysqlRemoteService mysqlRemoteService;

    @RequestMapping("/create/project/information")
    public String saveProjectBasicInfo(
            // 接收除了上传图片之外的其他普通数据
            ProjectVO projectVO,// 接收上传的头图
            MultipartFile headerPicture,
            // 接收上传的详情图片
            List<MultipartFile> detailPictureList,
            // 用来将收集了一部分数据的ProjectVO 对象存入Session 域
            HttpSession session,
            // 用来在当前操作失败后返回上一个表单页面时携带提示消息
            ModelMap modelMap
    ) throws IOException {
        // 一、完成头图上传
        // 1.获取当前headerPicture 对象是否为空
        boolean headerPictureIsEmpty = headerPicture.isEmpty();
        if(headerPictureIsEmpty) {
            // 2.如果没有上传头图则返回到表单页面并显示错误消息
            modelMap.addAttribute(CrowdConstantSon.ACCT_NAME_MESSAGE,
                    CrowdConstantSon.MESSAGE_HEADER_PIC_EMPTY);
            return "project-launch";
        }
        log.debug("头图片文件名:{}",headerPicture.getOriginalFilename());
        // 3.如果用户确实上传了有内容的文件，则执行上传
        ResultUtil<String> uploadHeaderPicResultEntity = OSSUploadUtile.uploadFileToOss(
                ossProperties.getEndpoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                headerPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                headerPicture.getOriginalFilename(),
                ossProperties.getObjectName());
        boolean result = uploadHeaderPicResultEntity.isResult();
            // 4.判断头图是否上传成功
        if(result) {
            // 5.如果成功则从返回的数据中获取图片访问路径
            String headerPicturePath = uploadHeaderPicResultEntity.getData();

            // 6.存入ProjectVO 对象中
            projectVO.setHeaderPicturePath(headerPicturePath);
        } else {
            // 7.如果上传失败则返回到表单页面并显示错误消息
            modelMap.addAttribute(CrowdConstantSon.ACCT_NAME_MESSAGE,
                    CrowdConstantSon.MESSAGE_HEADER_PIC_EMPTY);
            return "project-launch";
        }
        // 二、上传详情图片
        // 1.创建一个用来存放详情图片路径的集合

        List<String> detailPicturePathList = new ArrayList<String>();
        // 2.检查detailPictureList 是否有效
        if(detailPictureList == null || detailPictureList.size() == 0) {
            modelMap.addAttribute(CrowdConstantSon.ACCT_NAME_MESSAGE,
                    CrowdConstantSon.MESSAGE_HEADER_PIC_EMPTY);
            return "project-launch";
        }
            // 3.遍历detailPictureList 集合
        for (MultipartFile detailPicture : detailPictureList) {
            log.debug("详细信息当前文件名：{}",detailPicture.getOriginalFilename());
            // 4.当前detailPicture 是否为空
            if(detailPicture.isEmpty()) {
                // 5.检测到详情图片中单个文件为空也是回去显示错误消息
                modelMap.addAttribute(CrowdConstantSon.ACCT_NAME_MESSAGE,
                        CrowdConstantSon.MESSAGE_HEADER_PIC_EMPTY);
                return "project-launch";
            }
            // 6.执行上传
            ResultUtil<String> detailUploadResultEntity = OSSUploadUtile.uploadFileToOss(
                    ossProperties.getEndpoint(),
                    ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret(),
                    detailPicture.getInputStream(),
                    ossProperties.getBucketName(),
                    ossProperties.getBucketDomain(),
                    detailPicture.getOriginalFilename(),
                    ossProperties.getObjectName());
            // 7.检查上传结果
            boolean detailUploadResult = detailUploadResultEntity.isResult();
            if(detailUploadResult) {
                String detailPicturePath = detailUploadResultEntity.getData();
                // 8.收集刚刚上传的图片的访问路径
                detailPicturePathList.add(detailPicturePath);
            } else {
                // 9.如果上传失败则返回到表单页面并显示错误消息
                modelMap.addAttribute(CrowdConstantSon.ACCT_NAME_MESSAGE,
                        CrowdConstantSon.MESSAGE_HEADER_PIC_EMPTY);
                return "project-launch";
            }
        }
        // 10.将存放了详情图片访问路径的集合存入ProjectVO 中
        projectVO.setDetailPicturePathList(detailPicturePathList);
        // 三、后续操作
        // 1.将ProjectVO 对象存入Session 域
        session.setAttribute(CrowdConstantSon.ATTR_NAME_TEMPLE_PROJECT, projectVO);
        // 2.以完整的访问路径前往下一个收集回报信息的页面
        return "redirect:/project/return/info/page";
    }

    /**
     * 收集回报信息中的上传图片
     * @return
     */
    @ResponseBody
    @RequestMapping("/create/upload/return/picture.json")
    public ResultUtil<String>  uploadReturnPicture(@RequestParam("returnPicture") MultipartFile returnPicture) throws IOException {
        //上传汇报信息图片
        ResultUtil<String> uploadPath = OSSUploadUtile.uploadFileToOss(
                ossProperties.getEndpoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                returnPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                returnPicture.getOriginalFilename(),
                ossProperties.getObjectName());
        return uploadPath;
    }

    /**
     * 保存回报信息
     * @param returnVO
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping("/create/save/return.json")
    public ResultUtil<String> saveReturn(ReturnVO returnVO, HttpSession session){
        try {
            // 1.从 session 域中读取之前缓存的 ProjectVO 对象
            ProjectVO projectVO = (ProjectVO)
                    session.getAttribute(CrowdConstantSon.ATTR_NAME_TEMPLE_PROJECT);
            // 2.判断 projectVO 是否为 null
            if(projectVO == null) {
                return ResultUtil.fail(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
            }
            // 3.从 projectVO 对象中获取存储回报信息的集合
            List<ReturnVO> returnVOList = projectVO.getReturnVOList();
            // 4.判断 returnVOList 集合是否有效
            if(returnVOList == null || returnVOList.size() == 0) {
                // 5.创建集合对象对 returnVOList 进行初始化
                returnVOList = new ArrayList<>();
                // 6.为了让以后能够正常使用这个集合，设置到 projectVO 对象中
                projectVO.setReturnVOList(returnVOList);
            }
            // 7.将收集了表单数据的 returnVO 对象存入集合
            returnVOList.add(returnVO);
            // 8.把数据有变化的 ProjectVO 对象重新存入 Session 域，以确保新的数据最终能够存入 Redis
            session.setAttribute(CrowdConstantSon.ATTR_NAME_TEMPLE_PROJECT, projectVO);
            // 9.所有操作成功完成返回成功
            return ResultUtil.ok(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(e.getMessage());
        }
    }

    @RequestMapping("/create/confirm.html")
    public String saveConfirm(ModelMap modelMap, HttpSession session, MemberConfirmInfoVO
            memberConfirmInfoVO) {
        // 1.从 Session 域读取之前临时存储的 ProjectVO 对象
        ProjectVO projectVO = (ProjectVO)
                session.getAttribute(CrowdConstantSon.ATTR_NAME_TEMPLE_PROJECT);
        // 2.如果 projectVO 为 null
        if(projectVO == null) {
            throw new RuntimeException(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
        }
        // 3.将确认信息数据设置到 projectVO 对象中
        projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);
        // 4.从 Session 域读取当前登录的用户
        MemberVO memberVO = (MemberVO)
                session.getAttribute(CrowdConstantSon.ATTR_NAME_LOGIN_MEMBER);
        Integer memberId = memberVO.getId();
        // 5.调用远程方法保存 projectVO 对象
        ResultUtil<String> saveResultEntity =
                mysqlRemoteService.saveProjectVORemote(projectVO, memberId);
        // 6.判断远程的保存操作是否成功
        boolean result = saveResultEntity.isResult();
        if(!result) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,
                    saveResultEntity.getMessage());
            return "project-confirm";
        }
        // 7.将临时的 ProjectVO 对象从 Session 域移除
        session.removeAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);
        // 8.如果远程保存成功则跳转到最终完成页面
        return "redirect:/project/create/success";
    }

    /**
     * 获取当前项目的详细信息
     * @param projectId
     * @param model
     * @return
     */
    @RequestMapping("/get/project/detail/{projectId}")
    public String getProjectDetail(@PathVariable("projectId") Integer projectId, Model model) {
        ResultUtil<DetailProjectVO> resultEntity =
                mysqlRemoteService.getDetailProjectVORemote(projectId);
        if(resultEntity.isResult()) {
            DetailProjectVO detailProjectVO = resultEntity.getData();
            log.debug("远程获取到的项目详细信息:{}",detailProjectVO);
            model.addAttribute("detailProjectVO", detailProjectVO);
        }
        return "project-show-detail";
    }

}
