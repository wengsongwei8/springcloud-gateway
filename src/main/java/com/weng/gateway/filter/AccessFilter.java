package com.weng.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.weng.framework.core.model.ResponseData;
import com.weng.gateway.auth.AuthCheck;
import com.weng.gateway.utils.SsoTokenLoginHelper;
import com.weng.gateway.utils.TokenUtil;
import com.weng.sso.core.model.SsoUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


@Component
public class AccessFilter extends ZuulFilter {

	@Autowired
	private AuthCheck authCheck;
	
    private static Logger logger = LoggerFactory.getLogger(AccessFilter.class);

    private final String swaggerUrlSuffix = "/v2/api-docs";
    
    @Value("${security.ignore.url}")
    private String ignoreUrls;
    
    @Override
    public String filterType() {
    	return "pre";  
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String token = TokenUtil.getToken(request);;
        if(StringUtils.isEmpty(token)){
        	Object tokenTemp = request.getParameter("token");
        	if(tokenTemp!=null){
        		token = tokenTemp.toString();
        	}
        }
        if(request.getRequestURL().toString().endsWith(swaggerUrlSuffix)){
        	return null;
        }
        
        String[] ignoreUrlList = ignoreUrls.split("###");
        for(String ignoreUrl:ignoreUrlList){
        	String requestPath = request.getServletPath();
        	if(requestPath.indexOf(ignoreUrl)>=0){
        		return null;
        	}
        }
        
        
        //校验token,请求要求身份验证。 对于需要登录的网页，服务器返回此响应
        SsoUser curUser = null;
        if(token!=null){
        	try {
        		curUser = SsoTokenLoginHelper.loginCheck(token);
			} catch (Exception e) {
				logger.error("get token from cache error!please check redis service!",e);
			}
        	
        }else{
        	logger.info("token为空，请登陆后再访问!");
        }
        if (curUser == null) {
        	logger.info("token已过期，请登陆后再访问!");
            /**
             * 过滤器的具体逻辑。需要注意，这里我们通过ctx.setSendZuulResponse(false)令zuul过滤该请求，
             * 不对其进行路由，然后通过ctx.setResponseStatusCode(401)设置了其返回的错误码，
             * 当然我们也可以进一步优化我们的返回，比如，通过ctx.setResponseBody(body)对返回body内容进行编辑等
             */
            ctx.setSendZuulResponse(false);
            //过滤该请求，不往下级服务去转发请求，到此结束  
            ctx.setSendZuulResponse(false);  
//            ctx.setResponseStatusCode(401);
            ResponseData response = ResponseData.failedOfTokenError("token为空或不存在，请登陆后再访问!");
            ctx.setResponseBody(JSON.toJSONString(response));
            ctx.getResponse().setContentType("application/json;charset=UTF-8");  
            return null;
        } else{
        	/**
        	 * 判断当前用户是否拥有该URL的访问权限,如果没有，则过滤，不再向下转发
        	 */
        	String baseUrl = request.getRequestURI(); // 如/api/v1/user-center/user/list
        	baseUrl = trimReqeustUrl(baseUrl);
        	boolean isUrlAuth = authCheck.checkUrl(baseUrl, curUser);// 判断该URL是否有访问权限
        	if(!isUrlAuth){
        		ctx.setSendZuulResponse(false);
                //过滤该请求，不往下级服务去转发请求，到此结束  
                ctx.setSendZuulResponse(false);  
//                ctx.setResponseStatusCode(403);
                ResponseData response = ResponseData.failedOfPermission("抱歉，你没有该操作权限!");
                ctx.setResponseBody(JSON.toJSONString(response));
                ctx.getResponse().setContentType("application/json;charset=UTF-8");  
                return null;
        	}
        	
        	
        	
        	/**
        	 *    在基于 springcloud 构建的微服务系统中，通常使用网关zuul来进行一些用户验证等过滤的操作，
        	 * 比如 用户在 header 或者 url 参数中存放了 token ，网关层需要 用该 token 查出用户 的 userId ，
        	 * 并存放于 request 中，以便后续微服务可以直接使用而避免再去用 token 查询。
        	 * 	    但是获取到 request，但是在 request 中只有 getParameter() 而没有 setParameter() 方法，所以直接修改 url 参数不可行，
        	 * 另外在 reqeust 中可以虽然可以使用 setAttribute() ,但是可能由于作用域的不同，在这里设置的 attribute 在后续的微服务中是获取不到的
        	 * ，因此必须考虑另外的方式。
        	 */
//        	ctx.addZuulRequestHeader(WebUtils.ACCESS_USER_ID_FLAG, curUser.getId());
//        	ctx.addZuulRequestHeader(WebUtils.ACCESS_USER_NAME_FLAG, curUser.getUserName());
//        	ctx.getRequest().setAttribute("access_user_id", userId);
        }

        //添加Basic Auth认证信息
        return null;
    }

    /**
     * 裁剪请求URL，例如把/api/v1/user-center/user/list 转换为/user-center/user/list
     * @param requestUrl
     * @return
     */
    private static String trimReqeustUrl(String requestUrl){
    	if(requestUrl!=null && (requestUrl.indexOf("/api/v")>=0 || requestUrl.indexOf("/api/V")>=0)){
    		requestUrl = requestUrl.replace("/api/v", "");
    		requestUrl = requestUrl.replace("/api/V", "");
    		requestUrl = requestUrl.substring(requestUrl.indexOf("/"));
    	}
    	return requestUrl;
    }
    
    public static void main(String[] args){
//    	System.out.println(trimReqeustUrl("/api/v1/user-center/user/list"));
    }
    
    private Map<String, String> getHeadersInfo(HttpServletRequest request) {
    	Map<String, String> map = new HashMap<String, String>();
    	Enumeration headerNames = request.getHeaderNames();
    	while (headerNames.hasMoreElements()) {
    		String key = (String) headerNames.nextElement();
    		String value = request.getHeader(key);
    		map.put(key, value);
    	}
    	return map;
    }
    
    private String getBase64Credentials(String username, String password) {
        String plainCreds = username + ":" + password;
        byte[] plainCredsBytes = plainCreds.getBytes();
//        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        return new String(plainCredsBytes);
    }
}
