package com.weng.gateway.utils;

import com.weng.sso.core.config.SsoConfig;
import com.weng.sso.core.model.SsoUser;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/*
 *  token操作类
 * @author wengzhonghui
 * @date 12:54 2019/10/7
 */
public class SsoTokenLoginHelper {




    public static SsoUser loginCheck(String  token){
        return getUserInfoByToken(token);
    }

    public static SsoUser getUserInfoByToken(String  token){
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        SsoUser ssoUser = TokenStore.get(token);
        if(ssoUser == null){
            return null;
        }
        if ((System.currentTimeMillis() - ssoUser.getFreshTime()) > ssoUser.getExpireTimeLong()/2) {
            ssoUser.setFreshTime(System.currentTimeMillis());
            TokenStore.put(token, ssoUser);
        }
        return ssoUser;
    }




    public static SsoUser loginCheck(HttpServletRequest request){
        String headerSessionId = request.getHeader(SsoConfig.SSO_SESSIONID);
        return loginCheck(headerSessionId);
    }
}
