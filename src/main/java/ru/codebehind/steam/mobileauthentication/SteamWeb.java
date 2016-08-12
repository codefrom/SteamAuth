package ru.codebehind.steam.mobileauthentication;

import java.io.IOException;

import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ru.codebehind.okhttp.NameValuePairList;
import ru.codebehind.okhttp.cookies.SimpleCookieJar;

public class SteamWeb {
	public static class UserAgentInterceptor implements Interceptor {

	    private final String userAgent;

	    public UserAgentInterceptor(String userAgent) {
	        this.userAgent = userAgent;
	    }    
	    
	    public Response intercept(Chain chain) throws IOException {
	        Request originalRequest = chain.request();
	        Request requestWithUserAgent = originalRequest.newBuilder()
	            .header("User-Agent", userAgent)
	            .build();
	        return chain.proceed(requestWithUserAgent);
	    }
	}
	
	public static SimpleCookieJar StaticCookieJar = new SimpleCookieJar();
	public static String UserAgent = "Mozilla/5.0 (Linux; Android 4.4.4; Xperia S Build/KTU84P) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/33.0.0.0 Mobile Safari/537.36";
	
    /** 
     * Perform a mobile login request
     * @param url API url
     * @param method GET or POST
     * @param data Name-data pairs
     * @param cookies current cookie container
     * @param headers 
     * @return response body
     */
    public static String MobileLoginRequest(String url, 
    		String method, 
    		NameValuePairList data, 
    		CookieJar cookies, 
    		NameValuePairList headers) throws Throwable {
    	String dataString = "";
    	if (data != null) {
	    	HttpUrl.Builder builder = HttpUrl.parse("http://localhost").newBuilder();
	    	for (NameValuePairList.NameValuePair dataItem : data) {
	    		builder.addQueryParameter(dataItem.getName(), dataItem.getValue());			
			}
	    	HttpUrl httpUrl = builder.build();
			dataString = httpUrl.query();
    	}
		return DoRequest(url, 
        				 method,
        				 dataString,
        				 headers,
        				 null, 
        				 cookies, 
        				 APIEndpoints.COMMUNITY_BASE + "/mobilelogin?oauth_client_id=DE45CD61&oauth_scope=read_profile%20write_profile%20read_client%20write_client");
    }
    
    public static String DoRequest(String url, 
    		String method,
    		String dataString,
    		NameValuePairList headers, 
    		NameValuePairList params,
    		CookieJar cookieJar,
    		String referer) throws Throwable {
    	CookieJar realCookieJar = cookieJar;
    	if (realCookieJar == null) {
    		realCookieJar = StaticCookieJar;
    	}

    	OkHttpClient client = new OkHttpClient.Builder()
    			.addNetworkInterceptor(new UserAgentInterceptor(UserAgent))
    			.cookieJar(realCookieJar)
    			.build();
    	
    	HttpUrl.Builder uriBuilder = HttpUrl.parse(url).newBuilder();
    	if (params != null) {
	    	for (NameValuePairList.NameValuePair param : params) {
				uriBuilder.addQueryParameter(param.getName(), param.getValue());
			}
    	}

    	Request.Builder reqBuilder = new Request.Builder()
    			.url(uriBuilder.build())
    			.header("Accept", "text/javascript")
    			.addHeader("Accept", "text/html")
    			.addHeader("Accept", "application/xml")
    			.addHeader("Accept", "text/xml")
    			.addHeader("Accept", "*/*")
    			.header("User-Agent", UserAgent)
    			.header("Referer", referer);
    	
    	if (headers != null) {
	    	for (NameValuePairList.NameValuePair header : headers) {
	    		reqBuilder.addHeader(header.getName(), header.getValue());
			}
    	}
    	
    	if (method.equalsIgnoreCase("post")) {
    		if (dataString != null) {
    		reqBuilder.post(RequestBody.create(
					MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), 
					dataString));
    		}
		} else {
			reqBuilder.get();
		}
    	
    	// get response from server
    	try {
	    	Response res = client.newCall(reqBuilder.build()).execute();
	    	if (res.isRedirect()) {
	    		if (res.header("Location").equalsIgnoreCase("steammobile://lostauth"))
	    			throw new SteamGuardAccount.WGTokenExpiredException();
	    	}
	    	if (res.isSuccessful())
	    		return res.body().string();
    	} catch (IOException exc) {
    		exc.printStackTrace(); // TODO: fix
    	}
    	return "";
    }
}
