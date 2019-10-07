package com.weng.gateway.auth;

import java.util.Map;

/**
 * 对权限进行缓存操作的接口
 * @author wengzhonghui
 *
 */
public interface AuthCacheService {

   
    /**
     * 添加所有已定义的权限到缓存
     * @param priMap 权限集合，Key为url，value为编码
     * @throws Exception
     */
    public void addAllPri(Map<String, String> priMap) throws Exception;

    /**
     * 判断某个URL是否需要权限控制
     * @param url
     * @return
     * @throws Exception
     */
    public boolean checkPriIsExists(String url) throws Exception;

}
