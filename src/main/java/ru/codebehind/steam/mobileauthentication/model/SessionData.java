package ru.codebehind.steam.mobileauthentication.model;

import okhttp3.Cookie;
import ru.codebehind.okhttp.cookies.SimpleCookieJar;

public class SessionData {
    private String sessionID;
    private String steamLogin;
    private String steamLoginSecure;
    private String webCookie;
    private String oauthToken;
    private long steamID;
    
	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	public String getSteamLogin() {
		return steamLogin;
	}
	public void setSteamLogin(String steamLogin) {
		this.steamLogin = steamLogin;
	}
	public String getSteamLoginSecure() {
		return steamLoginSecure;
	}
	public void setSteamLoginSecure(String steamLoginSecure) {
		this.steamLoginSecure = steamLoginSecure;
	}
	public String getWebCookie() {
		return webCookie;
	}
	public void setWebCookie(String webCookie) {
		this.webCookie = webCookie;
	}
	public String getOAuthToken() {
		return oauthToken;
	}
	public void setOAuthToken(String oauthToken) {
		this.oauthToken = oauthToken;
	}
	public long getSteamID() {
		return steamID;
	}
	public void setSteamID(long steamID) {
		this.steamID = steamID;
	}

	public void addCookies(SimpleCookieJar cookies) {
        cookies.add(new Cookie.Builder().name("mobileClientVersion").value("0 (2.1.3)").path("/").domain("steamcommunity.com").build());
        cookies.add(new Cookie.Builder().name("mobileClient").value("android").path("/").domain("steamcommunity.com").build());

        cookies.add(new Cookie.Builder().name("steamid").value(Long.toString(steamID)).path("/").domain("steamcommunity.com").build());
        cookies.add(new Cookie.Builder().name("steamLogin").value(steamLogin).path("/").domain("steamcommunity.com").httpOnly().build());
        cookies.add(new Cookie.Builder().name("steamLoginSecure").value(steamLoginSecure).path("/").domain("steamcommunity.com").httpOnly().secure().build());

        cookies.add(new Cookie.Builder().name("Steam_Language").value("english").path("/").domain("steamcommunity.com").build());
        cookies.add(new Cookie.Builder().name("dob").value("english").path("/").domain("steamcommunity.com").build());
        cookies.add(new Cookie.Builder().name("sessionid").value(sessionID).path("/").domain("steamcommunity.com").build());
	}
}
