package com.taotao.sso.Interceptor;

import com.taotao.common.CookieUtils;
import com.taotao.common.HttpUtil;
import com.taotao.common.SystemConstants;
import com.taotao.common.TaotaoResult;
import com.taotao.pojo.TbUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class OrderInterceptor implements HandlerInterceptor {
    @Value("http://localhost:8084/page/showLogin")
    private String ssoLoginUrl;

    @Value("http://localhost:8084/user/token")
    private String ssoTokenUrl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        PrintWriter writer = response.getWriter();
        // 获取Cookie中的token
        String token = CookieUtils.getCookieValue(request, "TT_TOKEN");

        // 获取用户原本想去的页面
        //String redirectUrl = HttpUtil.getFullUrl(request);
       // String url = SSO_URL + "/page/login?redirect=" + request.getRequestURL().toString();
        // 如果用户没有token，重定向到登录页，并且附加原始url
        if(StringUtils.isBlank(token)) {
            //writer.println("<script>alert('会话超时，请重新登录！');location.href='"+loginUrl+"?redirectUrl="+uri+"';</script>");
            response.sendRedirect(ssoLoginUrl+"?redirect=" + request.getRequestURL().toString());
            return false;
        }

        // 拿从Cookie中获取的token信息请求SSO服务，获取真正的用户信息
        String jsonData = HttpUtil.doGet(ssoTokenUrl + "/" +token);
        try {
//            if(!(jsonData==null)){
//                TaotaoResult taotaoResult = TaotaoResult.formatToPojo(jsonData, TbUser.class);
//            }
            // 用户token正常，将token对应的用户信息保存到request作用域
            TaotaoResult taotaoResult = TaotaoResult.formatToPojo(jsonData, TbUser.class);
            if(taotaoResult.getStatus().equals(SystemConstants.TAOTAO_RESULT_STATUS_OK)) {
                TbUser tbUser = (TbUser) taotaoResult.getData();
                request.setAttribute("user", tbUser);
                return true;
            }
            // 用户会话过期后的处理
            response.sendRedirect("<script>alert('登录信息失效，请重新登录');window.location.href='"+ssoLoginUrl+"';</script>");
            return false;
        } catch (Exception e) {
            log.error("无法获取用户信息", e);
            return false;
        }
    }
}
