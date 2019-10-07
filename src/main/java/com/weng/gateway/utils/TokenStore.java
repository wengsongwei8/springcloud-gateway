package com.weng.gateway.utils;

import com.weng.framework.cache.redis.RedisClient;
import com.weng.sso.core.config.SsoConfig;
import com.weng.sso.core.model.SsoUser;

/*
 *  认证 信息存储  缓存
 * @author wengzhonghui
 * @date 11:58 2019/4/8
 */
public class TokenStore {

    private static int redisExpireMinite = SsoConfig.SSO_EXPIRE_TIME_LONG;    // 1440 minite, 24 hour


    /**
     * get
     *
     * @param storeKey
     * @return
     */
    public static SsoUser get(String storeKey) {

        String redisKey = redisKey(storeKey);
        Object objectValue = RedisClient.getObject(redisKey);
        if (objectValue != null) {
            SsoUser ssoUser = (SsoUser) objectValue;
            return ssoUser;
        }
        return null;
    }

    /**
     * remove
     * @param storeKey
     */
    public static void remove(String storeKey) {
        String redisKey = redisKey(storeKey);
        RedisClient redisClient = new RedisClient();
        redisClient.del(redisKey);
    }

    /**
     * put
     *
     * @param storeKey
     * @param ssoUser
     */
    public static void put(String storeKey, SsoUser ssoUser) {
        String redisKey = redisKey(storeKey);
        RedisClient.setObject(redisKey,ssoUser,redisExpireMinite);
    }

    private static String redisKey(String sessionId){
        return SsoConfig.SSO_SESSIONID.concat("#").concat(sessionId);
    }

}
