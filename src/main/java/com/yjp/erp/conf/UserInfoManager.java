package com.yjp.erp.conf;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.yjp.erp.constants.SystemConstant;
import com.yjp.erp.util.CookieUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

/**
 * @author wyf
 * @date 2019/4/17 下午 2:55
 **/
@Component
public class UserInfoManager {

    private static Cache<String, UserInfo> USER_INFO_CACHE = Caffeine.newBuilder()
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();

    /**
     * 初始化会话缓存
     *
     * @date 2019/4/17 下午 2:55
     */
    public void setUserInfo(String userId, UserInfo userInfo) {
        USER_INFO_CACHE.put(userId, userInfo);
    }

    /**
     * 从缓存获取用户信息
     * @date  2019/4/17 下午 2:55
     * @return com.yjp.erp.conf.UserInfo
     */
    public UserInfo getUserInfo() {
        String userId = getSessionId();
        return USER_INFO_CACHE.getIfPresent(userId);
    }

    /**
     * 获取会话Id =》userId编码（Base64）之后的值
     *
     * @return String 会话Id
     */
    private String getSessionId() {
        return CookieUtils.getCookie(SystemConstant.COOKIE_USERID,
                false,
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
    }

    /**
     * 删除会话
     */
    public void removeUserInfo() {
        String sessionId = getSessionId();
        USER_INFO_CACHE.invalidate(sessionId);
    }


}
