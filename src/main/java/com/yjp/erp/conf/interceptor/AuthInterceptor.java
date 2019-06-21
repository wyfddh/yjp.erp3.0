/*
 * Copyright © 2016 北京易酒批电子商务有限公司. All rights reserved.
 */
package com.yjp.erp.conf.interceptor;

import com.alibaba.fastjson.JSON;
import com.yjp.erp.conf.UserInfo;
import com.yjp.erp.conf.UserInfoManager;
import com.yjp.erp.conf.exception.BusinessException;
import com.yjp.erp.constants.SystemConstant;
import com.yjp.erp.model.vo.RetCode;
import com.yjp.erp.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private UserInfoManager userInfoManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {

        //请求controller中的方法名
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        //类路径
        String className = handlerMethod.getBean().getClass().getName();

        if (className.contains(SystemConstant.LOGIN_CONTROLLER) || className.contains(SystemConstant
                .KAPTCHACONTROLLER) || className.contains(SystemConstant.EXTERNAL_API_CONFIGCONTROLLER)) {
            return true;
        }

        UserInfo userInfo = userInfoManager.getUserInfo();

        if (ObjectUtils.isEmpty(userInfo)) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", 901);
            map.put("msg", "会话超时");
            String result = JSON.toJSONString(map);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            try (PrintWriter out = response.getWriter()) {
                out.append(result);
            } catch (IOException e) {
                throw new BusinessException(RetCode.LOGIN_OUT_TIME, "会话超时");
            }
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }


}
