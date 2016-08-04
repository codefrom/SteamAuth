package ru.codebehind.steam.mobileauthentication;

import java.net.CookieStore;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;

public class SteamWeb {
    /** 
     * Perform a mobile login request
     * @param url API url
     * @param method GET or POST
     * @param data Name-data pairs
     * @param cookies current cookie container
     * @param headers 
     * @return response body
     */
    public static String MobileLoginRequest(String url, String method, Map<String, String> data, CookieStore cookies, Map<String, String> headers) {
        //return Request(url, method, data, cookies, headers, APIEndpoints.COMMUNITY_BASE + "/mobilelogin?oauth_client_id=DE45CD61&oauth_scope=read_profile%20write_profile%20read_client%20write_client");
    }
    
    public static String Req(String url, String method, List<NameValuePair> data, CookieStore cookies, List<NameValuePair> headers, String referer)
    {
    	Request req;
    	URI uri = new URIBuilder(url).addParameters(data).build();
    	switch (url) {
			case "get":
				req = Request.Get(uri);
				break;
			case "post":
				req = Request.Post(uri);
				break;
		}

    	for (NameValuePair header : headers) {
    		req = req.addHeader(header.getName(), header.getValue());	
		}
    	
    	Executor.newInstance().use(cookies).execute(req);

        return Request(url, method, query, cookies, headers, referer);
    }
}
