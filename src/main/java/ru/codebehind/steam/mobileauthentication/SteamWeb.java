package ru.codebehind.steam.mobileauthentication;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SteamWeb {
	
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
    /*public static String MobileLoginRequest(String url, String method, List<NameValuePair> data, CookieStore cookies, List<NameValuePair> headers) throws Throwable {
        return Req(url, method, data, cookies, headers, APIEndpoints.COMMUNITY_BASE + "/mobilelogin?oauth_client_id=DE45CD61&oauth_scope=read_profile%20write_profile%20read_client%20write_client");
    }*/
    
    public static String DoRequest(String url, 
    		String method, 
    		String dataString, 
    		List<Map.Entry<String, String>> headers, 
    		List<Map.Entry<String, String>> params,
    		CookieManager cookieManager,
    		String referer) throws Throwable {
    	CookieManager realCookieManager = cookieManager;
    	if (realCookieManager == null) {
    		realCookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
    	}

    	OkHttpClient client = new OkHttpClient.Builder()
    			.cookieJar(new JavaNetCookieJar(realCookieManager))
    			.build();
    	
    	HttpUrl.Builder uriBuilder = HttpUrl.parse(url).newBuilder();
    	for (Map.Entry<String, String> param : params) {
			uriBuilder.addQueryParameter(param.getKey(), param.getValue());
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
    	
    	for (Map.Entry<String, String> header : headers) {
    		reqBuilder.addHeader(header.getKey(), header.getValue());
		}
    	
    	if (method.equalsIgnoreCase("post")) {
    		reqBuilder.post(RequestBody.create(
					MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), 
					dataString));
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
