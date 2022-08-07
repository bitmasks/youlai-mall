package com.youlai.common.web.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youlai.common.constant.SecurityConstants;
import com.youlai.common.result.ResultCode;
import com.youlai.common.web.domain.CurrUserInfo;
import com.youlai.common.web.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 用户会话工具类
 *
 * @author haoxr
 * @date 2022/2/5
 */
@Component
@Slf4j
public class UserSessionUtils {

    private static RedisTemplate staticRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * @Constructor > @Autowired > @PostConstruct
     */
    @PostConstruct
    public void init() {
        staticRedisTemplate = redisTemplate;
    }


    public static String getToken() {
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
                .getHeader(SecurityConstants.ACCESS_TOKEN_KEY);
        if (StrUtil.isBlank(token)) {
            throw new BusinessException(ResultCode.TOKEN_INVALID_OR_EXPIRED);
        }
        return token;
    }


    /**
     * 解析JWT获取用户ID
     *
     * @return
     */
    public static CurrUserInfo getCurrUser() {

        String token = getToken();
        Object obj = staticRedisTemplate.opsForValue().get(SecurityConstants.OAUTH_TOKEN_PREFIX +"access:"+ token);
        if (obj == null) {
            throw new BusinessException(ResultCode.TOKEN_INVALID_OR_EXPIRED);
        }


        LinkedHashMap map = (LinkedHashMap)obj;

        CurrUserInfo currUserInfo=new CurrUserInfo();
        currUserInfo.setUserId(Convert.toLong(map.get("userId")));
        currUserInfo.setUsername(Convert.toStr(map.get("username")));
        currUserInfo.setDeptId(Convert.toLong(map.get("deptId")));
        currUserInfo.setAuthorities(Convert.toList(String.class,JSONUtil.parseArray(map.get("authorities"))));
        return currUserInfo;
    }

    public static Long getUserId() {
        return getCurrUser().getUserId();
    }

    public static Long getDeptId() {
        return getCurrUser().getDeptId();
    }

    public static String getUsername() {
        return getCurrUser().getUsername();
    }

    public static List<String> getRoles() {
        return getCurrUser().getAuthorities();
    }

    public static boolean isRoot() {
        List<String> roles = getRoles();
        return CollectionUtil.isNotEmpty(roles) && roles.contains("ROOT");
    }

}
