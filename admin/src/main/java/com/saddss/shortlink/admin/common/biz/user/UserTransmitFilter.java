package com.saddss.shortlink.admin.common.biz.user;

import com.alibaba.fastjson2.JSON;
import com.saddss.shortlink.admin.common.constant.RedisCacheConstant;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;

/**
 * 用户信息传输过滤器
 */
@RequiredArgsConstructor
public class UserTransmitFilter implements Filter {
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String username = httpServletRequest.getHeader("username");
        String token = httpServletRequest.getHeader("token");
        String requestURI = ((HttpServletRequest) servletRequest).getRequestURI();
        if (!requestURI.equals("/api/short-link/admin/v1/user/login")){
            Object userDO = stringRedisTemplate.opsForHash().get(RedisCacheConstant.USER_LOGIN_KEY + username, token);
            if (userDO != null){
                UserInfoDTO userInfoDTO = JSON.parseObject(userDO.toString(), UserInfoDTO.class);
                UserContext.setUser(userInfoDTO);
            }
        }
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserContext.removeUser();
        }
    }

//    @SneakyThrows
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
//        String username = httpServletRequest.getHeader("username");
//        if (StrUtil.isNotBlank(username)) {
//            String userId = httpServletRequest.getHeader("userId");
//            String realName = httpServletRequest.getHeader("realName");
//            UserInfoDTO userInfoDTO = new UserInfoDTO(userId, username, realName);
//            UserContext.setUser(userInfoDTO);
//        }
//        try {
//            filterChain.doFilter(servletRequest, servletResponse);
//        } finally {
//            UserContext.removeUser();
//        }
//    }
}
