package ru.codebehind.steam.mobileauthentication;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import ru.codebehind.okhttp.NameValuePairList;
import ru.codebehind.okhttp.cookies.SimpleCookieJar;
import ru.codebehind.steam.mobileauthentication.model.LoginRequest;
import ru.codebehind.steam.mobileauthentication.model.LoginResult;
import ru.codebehind.steam.mobileauthentication.model.RSAResponse;
import ru.codebehind.toolbelt.JacksonSerive;

public class UserLoginService {
    private SimpleCookieJar _cookies = new SimpleCookieJar();
    
    public LoginResult DoLogin(LoginRequest request)
    {
    	NameValuePairList postData = new NameValuePairList(); 
        String response = null;

        if (_cookies.count() == 0)
        {
            //Generate a SessionID
        	_cookies.add((new Cookie.Builder()).name("mobileClientVersion").value("0 (2.1.3)").path("/").domain(".steamcommunity.com").build());
        	_cookies.add((new Cookie.Builder()).name("mobileClient").value("android").path("/").domain(".steamcommunity.com").build());
        	_cookies.add((new Cookie.Builder()).name("Steam_Language").value("english").path("/").domain(".steamcommunity.com").build());

        	NameValuePairList headers = new NameValuePairList();
            headers.add("X-Requested-With", "com.valvesoftware.android.steam.community");

            SteamWeb.MobileLoginRequest("https://steamcommunity.com/login?oauth_client_id=DE45CD61&oauth_scope=read_profile%20write_profile%20read_client%20write_client", 
            		"GET", 
            		null, 
            		_cookies, 
            		headers);
        }

        postData.add("username", request.getUsername());
        response = SteamWeb.MobileLoginRequest(APIEndpoints.COMMUNITY_BASE + "/login/getrsakey", 
        		"POST", 
        		postData, 
        		_cookies, 
        		null);
        if (response == null || response.contains("<BODY>\nAn error occurred while processing your request.")) 
        	return LoginResult.GeneralFailure;
        
        RSAResponse rsaResponse = JacksonSerive.Deserialize(RSAResponse.class, response);

        if (!rsaResponse.isSuccess())
            return LoginResult.BadRSA;
        
        String encryptedPassword;
        byte[] passwordBytes = request.getPassword().getBytes("ASCII");
        try {
        	BigInteger modulus = new BigInteger(rsaResponse.getModulus(), 16);
        	BigInteger exponent = new BigInteger(rsaResponse.getExponent(), 16);
        			
        	RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);

        	KeyFactory factory = KeyFactory.getInstance("RSA");
        			
        	PublicKey pub = factory.generatePublic(spec);
        	
        	Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
        	cipher.init(Cipher.ENCRYPT_MODE, pub);

        	encryptedPassword = new String(Base64.getEncoder().encode(cipher.doFinal(passwordBytes)));
    	} catch(Exception e) {
    	}        

        postData.clear();
        postData.add("username", request.getUsername());
        postData.add("password", encryptedPassword);

        postData.add("twofactorcode", request.getTwoFactorCode());

        if (request.isRequiresCaptcha()) { 
        	postData.add("captchagid", request.getCaptchaGID());
        	postData.Add("captcha_text", request.getCaptchaText());
        } else {
        	postData.add("captchagid", "-1");
        	postData.Add("captcha_text", "");
    	}

        postData.Add("emailsteamid", (this.Requires2FA || this.RequiresEmail) ? this.SteamID.ToString() : "");
        postData.Add("emailauth", this.RequiresEmail ? this.EmailCode : "");

        postData.Add("rsatimestamp", rsaResponse.Timestamp);
        postData.Add("remember_login", "false");
        postData.Add("oauth_client_id", "DE45CD61");
        postData.Add("oauth_scope", "read_profile write_profile read_client write_client");
        postData.Add("loginfriendlyname", "#login_emailauth_friendlyname_mobile");
        postData.Add("donotcache", Util.GetSystemUnixTime().ToString());

        response = SteamWeb.MobileLoginRequest(APIEndpoints.COMMUNITY_BASE + "/login/dologin", "POST", postData, cookies);
        if (response == null) return LoginResult.GeneralFailure;

        var loginResponse = JsonConvert.DeserializeObject<LoginResponse>(response);

        if (loginResponse.Message != null && loginResponse.Message.Contains("Incorrect login"))
        {
            return LoginResult.BadCredentials;
        }

        if (loginResponse.CaptchaNeeded)
        {
            this.RequiresCaptcha = true;
            this.CaptchaGID = loginResponse.CaptchaGID;
            return LoginResult.NeedCaptcha;
        }

        if (loginResponse.EmailAuthNeeded)
        {
            this.RequiresEmail = true;
            this.SteamID = loginResponse.EmailSteamID;
            return LoginResult.NeedEmail;
        }

        if (loginResponse.TwoFactorNeeded && !loginResponse.Success)
        {
            this.Requires2FA = true;
            return LoginResult.Need2FA;
        }

        if (loginResponse.Message != null && loginResponse.Message.Contains("too many login failures"))
        {
            return LoginResult.TooManyFailedLogins;
        }

        if (loginResponse.OAuthData == null || loginResponse.OAuthData.OAuthToken == null || loginResponse.OAuthData.OAuthToken.Length == 0)
        {
            return LoginResult.GeneralFailure;
        }

        if (!loginResponse.LoginComplete)
        {
            return LoginResult.BadCredentials;
        }
        else
        {
            var readableCookies = cookies.GetCookies(new Uri("https://steamcommunity.com"));
            var oAuthData = loginResponse.OAuthData;

            SessionData session = new SessionData();
            session.OAuthToken = oAuthData.OAuthToken;
            session.SteamID = oAuthData.SteamID;
            session.SteamLogin = session.SteamID + "%7C%7C" + oAuthData.SteamLogin;
            session.SteamLoginSecure = session.SteamID + "%7C%7C" + oAuthData.SteamLoginSecure;
            session.WebCookie = oAuthData.Webcookie;
            session.SessionID = readableCookies["sessionid"].Value;
            this.Session = session;
            this.LoggedIn = true;
            return LoginResult.LoginOkay;
        }
}
}
