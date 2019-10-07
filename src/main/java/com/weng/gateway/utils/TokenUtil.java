package com.weng.gateway.utils;

import com.weng.sso.core.config.SsoConfig;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wengzhonghui
 * @date 2019/5/22 9:37
 */
public class TokenUtil {

    public static String getToken(HttpServletRequest request){
        Map<String, String> headers =  getHeadersInfo(request);
        if(headers!=null && headers.size()>0){
            return headers.get(SsoConfig.TOKEN_PARAM_NAME);
        }
        return "";
    }

    /*
     * 获取头信息
     * @param [request]
     * @return
     */
    public static Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }
}
