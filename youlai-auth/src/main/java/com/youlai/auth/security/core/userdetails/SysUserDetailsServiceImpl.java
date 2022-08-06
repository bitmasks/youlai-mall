package com.youlai.auth.security.core.userdetails;

import cn.hutool.core.collection.CollectionUtil;
import com.youlai.admin.api.UserFeignClient;
import com.youlai.admin.dto.UserAuthDTO;
import com.youlai.auth.common.enums.PasswordEncoderTypeEnum;
import com.youlai.common.constant.GlobalConstants;
import com.youlai.common.result.Result;
import com.youlai.common.result.ResultCode;
import com.youlai.common.security.userdetails.user.SysUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统用户体系业务类
 *
 * @author <a href="mailto:xianrui0365@163.com">haoxr</a>
 */
@Service("sysUserDetailsService")
@Slf4j
@RequiredArgsConstructor
public class SysUserDetailsServiceImpl implements UserDetailsService {

    private final UserFeignClient userFeignClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserDetails userDetails = null;
        Result<UserAuthDTO> result = userFeignClient.getUserByUsername(username);
        if (Result.isSuccess(result)) {
            UserAuthDTO user = result.getData();
            if (null != user) {
                userDetails = new SysUserDetails();
                userDetails.setUserId(user.getUserId());
                userDetails.setUsername(user.getUsername());
                userDetails.setDeptId(user.getDeptId());
                userDetails.setPassword(PasswordEncoderTypeEnum.BCRYPT.getPrefix() + user.getPassword());
                userDetails.setEnabled(GlobalConstants.STATUS_YES.equals(user.getStatus()));
                if (CollectionUtil.isNotEmpty(user.getRoles())) {
                    List<SimpleGrantedAuthority> authorities = user.getRoles()
                            .stream().map(role -> new SimpleGrantedAuthority(role))
                            .collect(Collectors.toList());
                    userDetails.setAuthorities(authorities);
                }
            }
        }
        if (userDetails == null) {
            throw new UsernameNotFoundException(ResultCode.USER_NOT_EXIST.getMsg());
        } else if (!userDetails.isEnabled()) {
            throw new DisabledException("该账户已被禁用!");
        } else if (!userDetails.isAccountNonLocked()) {
            throw new LockedException("该账号已被锁定!");
        } else if (!userDetails.isAccountNonExpired()) {
            throw new AccountExpiredException("该账号已过期!");
        }
        return userDetails;
    }

}
