package ru.codebehind.okhttp.cookies;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.CookieJar;

public class SimpleCookieJar implements CookieJar {
	private static final Cookie EMPTY_COOKIE = new Cookie.Builder().name("DUMMY").value("DUMMY").domain("DUMMY").build(); 
	ArrayList<Cookie> cookies = new ArrayList<Cookie>();
	
	private void removeExpiredCookies() {
		for (Iterator<Cookie> iterator = cookies.iterator(); iterator.hasNext();) {
			Cookie cookie = iterator.next();
			if (cookie.expiresAt() < (new Date()).getTime())
				iterator.remove();
		}
	}
	
	public List<Cookie> loadForRequest(HttpUrl request) {
		removeExpiredCookies();
		ArrayList<Cookie> resultCookies = new ArrayList<Cookie>();
		for (Cookie cookie : cookies) {
			if (cookie.matches(request) &&
				cookie.expiresAt() >= (new Date()).getTime())
				resultCookies.add(cookie);
		}
		return resultCookies;
	}

	public void saveFromResponse(HttpUrl response, List<Cookie> newCookies) {
		removeExpiredCookies();
		cookies.addAll(newCookies);
	}

	public int count() {
		return cookies.size();
	}

	public void add(Cookie cookie) {
		cookies.add(cookie);
	}
	
	public Cookie get(String name) {
		for (Cookie cookie : cookies) {
			if (cookie.name().equalsIgnoreCase(name))
				return cookie;
		}
		return EMPTY_COOKIE;
	}
}