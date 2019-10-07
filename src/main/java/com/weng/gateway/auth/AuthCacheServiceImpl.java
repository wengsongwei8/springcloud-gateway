package com.weng.gateway.auth;

import com.weng.framework.cache.redis.RedisClient;
import com.weng.gateway.utils.CacheConstants;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class AuthCacheServiceImpl implements AuthCacheService {

	private static int redisExpireMinite = 24*60*60*1000;    // 1440 minite, 24 hour

	@Override
	public void addAllPri(Map<String,String> priMap) throws Exception {

		RedisClient.setObject(CacheConstants.CACHE_ALL_PRI_KEY,priMap,redisExpireMinite);
	}

	@Override
	public boolean checkPriIsExists(String url) throws Exception {
		Map<String,String> priMap = (Map<String,String>)RedisClient.getObject(CacheConstants.CACHE_ALL_PRI_KEY);
		if(url!=null && priMap!=null && priMap.containsValue(url.trim())){
			return true;
		}
		return false;
	}

    

}
