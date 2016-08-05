package ru.codebehind.steam.mobileauthentication;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;

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
    public static String MobileLoginRequest(String url, String method, List<NameValuePair> data, CookieStore cookies, List<NameValuePair> headers) throws Throwable {
        return Req(url, method, data, cookies, headers, APIEndpoints.COMMUNITY_BASE + "/mobilelogin?oauth_client_id=DE45CD61&oauth_scope=read_profile%20write_profile%20read_client%20write_client");
    }
    
    public static String InnerRequest(String url, 
    		String method, 
    		String dataString, 
    		List<Map.Entry<String, String>> headers, 
    		String referer) {
    	CookieManager cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
    	
    	OkHttpClient client = new OkHttpClient.Builder()
    			.cookieJar(new JavaNetCookieJar(cookieManager))
    			.build();
    	
    	HttpUrl.Builder uriBuilder = HttpUrl.parse(url).newBuilder();
    	Request.Builder reqBuilder = new Request.Builder()
    			.header("Accept", "text/javascript")
    			.addHeader("Accept", "text/html")
    			.addHeader("Accept", "application/xml")
    			.addHeader("Accept", "text/xml")
    			.addHeader("Accept", "*/*")
    			.header("User-Agent", UserAgent);
    	
    	for (Map.Entry<String, String> header : headers) {
    		reqBuilder.addHeader(header.getKey(), header.getValue());
		}
    	
    	switch (method) {
		case "get":
			break;
		case "post":
			break;
		default:
			break;
		}
//    	Request req = new Request.Builder().;
    }
}
