package com.yama.crowd.constant;

/**
 * 定义通用常量
 */
public class CrowdConstant {

    public static final String ADMIN_LOGIN_MESSAGES="登陆异常请检查账户与密码，如未注册，请注册后登陆";

    public static final String MESSAGE_LOGIN_ACCt_ALREADY_IN_USE="当前用户已存在";
    public static final String MESSAGE_ACCESS_FORBIDEN = "请登陆以后再访问";

    public static final String ATTR_NAME_EXCEPTION = "exception";
    public static final String ATTR_NAME_LOGIN_ADMIN = "LoginAdmin";
    public static final String ATTR_NAME_PAGE_INFO = "pageInfo";
    public static final String ATTR_NAME_ADMIN = "admin";
    public static final String MESSAGE_LOGIN_FAILED = "登录失败！请确认账号密码是否正确!";
    public static final String MESSAGE_STRING_INVALIDATE = "字符串不合法!";
    public static final String MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE = "系统数据错误，登陆账号不唯一";
    public static final String MESSAGE_SYSTEM_EXCEPTIONS = "系统异常信息";
    public static final String MESSAGE_USER_UPDATE = "用户更新时失败，请重新操作";
    //登录的用户名
    public static final String LOGIN_ADMIN_NAME = "loginAdmin";

    public static final String MESSAGE_ACCESS_FORBIDDEN = "还未登录，禁止访问受保护资源！";
    public static final String NAME_PAGE_INFO = "pageInfo";
    public static final String REDIS_CODE_PREFIX = "REDIS_CODE_PREFIX" ;
    public static final String ATTR_NAME_MESSAGE = "message";
    public static final String MESSAGE_CODE_NOT_EXIST = "验证码无效！请检查是否输入了正确的手机号";
    public static final String MESSAGE_CODE_INVALID = "验证码错误";
    public static final String ATTR_NAME_LOGIN_MEMBER = "loginMember";
    public static final String MESSAGE_HEADER_PIC_EMPTY = "头图不能为空！";
    public static final String MESSAGE_HEADER_PIC_UPLOAD_FAILED = "头图上传失败，请重试！";
    public static final String MESSAGE_DETAIL_PIC_EMPTY = "详情图片不能为空！";
    public static final String MESSAGE_DETAIL_PIC_UPLOAD_FAILED = "详情图片上传失败，请重试！";
    public static final String ATTR_NAME_TEMPLE_PROJECT = "templeProject";
    public static final String MESSAGE_RETURN_PIC_EMPTY = "上传回报图片不能为空！";
    public static final String MESSAGE_TEMPLE_PROJECT_MISSING = "临时ProjectVO对象未找到！";
    public static final String ATTR_NAME_PORTAL_TYPE_LIST = "portal_type_list";
    public static final String ATTR_NAME_DETAIL_PROJECT = "detailProjectVO";
}
