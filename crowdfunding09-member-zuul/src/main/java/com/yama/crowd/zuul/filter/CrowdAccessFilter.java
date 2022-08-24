package com.yama.crowd.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.yama.crowd.constant.CrowdConstant;
import com.yama.crowd.constant.CrowdConstantSon;
import com.yama.crowd.zuul.util.AccessPassResources;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 设置过滤器，进行登陆检查
 * zuul的过滤器的一切基础都是基于对象：RequestContext
 * RequestContex使用ConcurrentHashMap的扩展，其中保存 request, response,  state information and data
 * 提供给zuulFilter使用。
 */
@Component
public class CrowdAccessFilter extends ZuulFilter {
    /**
     * 当前方法用来判断是否需要进行过滤
     * @return
     */
    @Override
    public boolean shouldFilter() {

        // 1.获取RequestContext 对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        // 2.通过RequestContext 对象获取当前请求对象（框架底层是借助ThreadLocal 从当前线程上获取事先绑定的Request对象）
        //每个请求，具有一个单独的线程，因此使用线程本地变量来绑定对象。
        HttpServletRequest request = requestContext.getRequest();
        // 3.获取servletPath 值
        String servletPath = request.getServletPath();
        // 4.根据servletPath 判断当前请求是否对应可以直接放行的特定功能
        boolean containsResult = AccessPassResources.PASS_RES_SET.contains(servletPath);
        if (containsResult) {
            // 5.如果当前请求是可以直接放行的特定功能请求则返回false 放行
            return false;
        }
        // 5.判断当前请求是否为静态资源
        // 工具方法返回true：说明当前请求是静态资源请求，取反为false 表示放行不做登录检查
        // 工具方法返回false：说明当前请求不是可以放行的特定请求也不是静态资源，取反为true 表示需要做登录检查
        return !AccessPassResources.judgeCurrentServletPathWetherStaticResource(servletPath);
    }

    /**
     * 执行过滤操作
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        // 1.获取当前请求对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        // 2.获取当前Session 对象
        HttpSession session = request.getSession();
        // 3.尝试从Session 对象中获取已登录的用户
        Object loginMember = session.getAttribute(CrowdConstantSon.ACCT_NAME_LOGIN_MEMEBER);
        // 4.判断loginMember 是否为空
        if(loginMember == null) {
            // 5.从requestContext 对象中获取Response 对象
            HttpServletResponse response = requestContext.getResponse();
            // 6.将提示消息存入Session 域
            session.setAttribute(CrowdConstantSon.ACCT_NAME_MESSAGE,
                    CrowdConstant.MESSAGE_ACCESS_FORBIDEN);
                // 7.重定向到auth-consumer 工程中的登录页面
            try {
                response.sendRedirect("/auth/member/to/login/page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 返回当前过滤器类型
     * 可选类型包括：pre、route、post、static
     * 如果需要在目标微服务前面执行过滤操作，选用 pre 类型
     * @return
     */
    @Override
    public String filterType() {
        // 这里返回“pre”意思是在目标微服务前执行过滤
        return "pre";
    }

    /**
     * 过滤器执行顺序
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }
}
