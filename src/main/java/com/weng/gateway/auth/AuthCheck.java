package com.weng.gateway.auth;

import com.weng.sso.core.model.SsoUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 权限检查
 * @author wengzhonghui
 *
 */
@Component
public class AuthCheck {

	private static final Logger logger = LoggerFactory.getLogger(AuthCheck.class);
	@Autowired
	private AuthCacheService authCacheService;
	
	public boolean checkUrl(String baseUrl, SsoUser curUser){
		
		try {
    		// 缓存中查询当前访问的URL是否需要做权限控制
    		boolean isUrlAuth = authCacheService.checkPriIsExists(baseUrl);
    		if(isUrlAuth){
    			if(curUser!=null && curUser.getPris()!=null && curUser.getPris().containsValue(baseUrl)){
    				// 如果该URL权限受控，而且用户也拥有该 权限
    				return true;
    			}else{
    				return false;
    			}
    		}
		} catch (Exception e) {
			logger.error("check url pri from cache error!please check redis service!",e);
		}
		return true;
	}
}
