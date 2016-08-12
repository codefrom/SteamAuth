package ru.codebehind.steam.mobileauthentication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import okhttp3.Cookie;
import ru.codebehind.okhttp.NameValuePairList;
import ru.codebehind.okhttp.cookies.SimpleCookieJar;
import ru.codebehind.steam.mobileauthentication.model.LoginRequest;
import ru.codebehind.steam.mobileauthentication.model.LoginResult;
import ru.codebehind.steam.mobileauthentication.model.LoginResultState;
import ru.codebehind.steam.mobileauthentication.model.SessionData;
import ru.codebehind.toolbelt.JsonHelper;

public class UserLoginService {

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class LoginResponse
    {
        public static class OAuthDeserializer extends StdDeserializer<LoginResponse.OAuth> {
            private static final long serialVersionUID = 8032829741835168912L;

            public OAuthDeserializer() { 
                this(null); 
            } 
         
            public OAuthDeserializer(Class<?> vc) { 
                super(vc); 
            }
            
            @Override
            public OAuth deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException, JsonProcessingException {
                try {
                    return JsonHelper.Deserialize(OAuth.class, p.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }    
        }
        
        @JsonIgnoreProperties(ignoreUnknown=true)
        public static class OAuth
        {
            @JsonProperty(value="steamid")
            private long steamID;

            @JsonProperty(value="oauth_token")
            private String oauthToken;
            
            @JsonProperty(value="wgtoken")
            private String steamLogin;

            @JsonProperty(value="wgtoken_secure")
            private String steamLoginSecure;

            @JsonProperty(value="webcookie")
            private String webcookie;            

			public long getSteamID() {
				return steamID;
			}

			public void setSteamID(long steamID) {
				this.steamID = steamID;
			}

			public String getOAuthToken() {
				return oauthToken;
			}

			public void setOAuthToken(String oauthToken) {
				this.oauthToken = oauthToken;
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

			public String getWebcookie() {
				return webcookie;
			}

			public void setWebcookie(String webcookie) {
				this.webcookie = webcookie;
			}            
        }
        
        @JsonProperty(value="success")
        private boolean success;

        @JsonProperty(value="login_complete")
        private boolean loginComplete;

        @JsonProperty(value="oauth")
        @JsonDeserialize(using=LoginResponse.OAuthDeserializer.class)
        private OAuth OAuthData;
        
        @JsonProperty(value="captcha_needed")
        private boolean captchaNeeded;

        @JsonProperty(value="captcha_gid")
        private String captchaGID;

        @JsonProperty(value="emailsteamid")
        private long emailSteamID;

        @JsonProperty(value="emailauth_needed")
        private boolean emailAuthNeeded;

        @JsonProperty(value="requires_twofactor")
        private boolean twoFactorNeeded;

        @JsonProperty(value="message")
        private String message;

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public boolean isLoginComplete() {
			return loginComplete;
		}

		public void setLoginComplete(boolean loginComplete) {
			this.loginComplete = loginComplete;
		}

		public OAuth getOAuthData() {
			return OAuthData;
		}

		public void setOAuthData(OAuth oAuthData) {
			OAuthData = oAuthData;
		}

		public boolean isCaptchaNeeded() {
			return captchaNeeded;
		}

		public void setCaptchaNeeded(boolean captchaNeeded) {
			this.captchaNeeded = captchaNeeded;
		}

		public String getCaptchaGID() {
			return captchaGID;
		}

		public void setCaptchaGID(String captchaGID) {
			this.captchaGID = captchaGID;
		}

		public long getEmailSteamID() {
			return emailSteamID;
		}

		public void setEmailSteamID(long emailSteamID) {
			this.emailSteamID = emailSteamID;
		}

		public boolean isEmailAuthNeeded() {
			return emailAuthNeeded;
		}

		public void setEmailAuthNeeded(boolean emailAuthNeeded) {
			this.emailAuthNeeded = emailAuthNeeded;
		}

		public boolean isTwoFactorNeeded() {
			return twoFactorNeeded;
		}

		public void setTwoFactorNeeded(boolean twoFactorNeeded) {
			this.twoFactorNeeded = twoFactorNeeded;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
    }
    
    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class RSAResponse
    {
        @JsonProperty(value="success")
        private boolean success;

        @JsonProperty(value="publickey_exp")
        private String exponent;

        @JsonProperty(value="publickey_mod")
        private String modulus;

        @JsonProperty(value="timestamp")
        private String timestamp;

        @JsonProperty(value="steamid")
        private long steamID;

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public String getExponent() {
			return exponent;
		}

		public void setExponent(String exponent) {
			this.exponent = exponent;
		}

		public String getModulus() {
			return modulus;
		}

		public void setModulus(String modulus) {
			this.modulus = modulus;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}

		public long getSteamID() {
			return steamID;
		}

		public void setSteamID(long steamID) {
			this.steamID = steamID;
		}
    }	
	
    private SimpleCookieJar _cookies = new SimpleCookieJar();
    
    public LoginResult DoLogin(LoginRequest request) throws Throwable {
    	NameValuePairList postData = new NameValuePairList(); 
        String response = null;

        if (_cookies.count() == 0)
        {
            //Generate a SessionID
        	_cookies.add((new Cookie.Builder()).name("mobileClientVersion").value("0 (2.1.3)").path("/").domain("steamcommunity.com").build());
        	_cookies.add((new Cookie.Builder()).name("mobileClient").value("android").path("/").domain("steamcommunity.com").build());
        	_cookies.add((new Cookie.Builder()).name("Steam_Language").value("english").path("/").domain("steamcommunity.com").build());

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
        	return LoginResult.GENERAL_FAILURE;
        
        RSAResponse rsaResponse = JsonHelper.Deserialize(RSAResponse.class, response);

        if (!rsaResponse.isSuccess())
            return LoginResult.BAD_RSA;
        
        String encryptedPassword = getSecurePassword(request.getPassword(), rsaResponse.getModulus(), rsaResponse.getExponent());        

        postData.clear();
        postData.add("username", request.getUsername());
        postData.add("password", encryptedPassword);

        postData.add("twofactorcode", request.getTwoFactorCode());

        if (request.isRequiresCaptcha()) { 
        	postData.add("captchagid", request.getCaptchaGID());
        	postData.add("captcha_text", request.getCaptchaText());
        } else {
        	postData.add("captchagid", "-1");
        	postData.add("captcha_text", "");
    	}
        if (request.isRequires2FA() || request.isRequiresEmail()) {
            postData.add("emailsteamid", Long.toString(request.getSteamID()));        	
        } else {
            postData.add("emailsteamid", "");       	
        }

        if (request.isRequiresEmail()) {
        	postData.add("emailauth", request.getEmailCode());
        } else {
            postData.add("emailsteamid", "");
        }

        postData.add("rsatimestamp", rsaResponse.getTimestamp());
        postData.add("remember_login", "false");
        postData.add("oauth_client_id", "DE45CD61");
        postData.add("oauth_scope", "read_profile write_profile read_client write_client");
        postData.add("loginfriendlyname", "#login_emailauth_friendlyname_mobile");
        postData.add("donotcache", Long.toString(new Date().getTime()));

        response = SteamWeb.MobileLoginRequest(APIEndpoints.COMMUNITY_BASE + "/login/dologin", 
        		"POST", 
        		postData, 
        		_cookies,
        		null);
        if (response == null)
        	return LoginResult.GENERAL_FAILURE;

        LoginResponse loginResponse = JsonHelper.Deserialize(LoginResponse.class, response);

        LoginResult result;

        if (loginResponse.getMessage() != null && loginResponse.getMessage().contains("Incorrect login")) {
            return LoginResult.BAD_CREDENTIALS;
        }

        if (loginResponse.isCaptchaNeeded()) {
        	result = new LoginResult(LoginResultState.NEED_CAPTCHA);
        	result.setRequiresCaptcha(true);
        	result.setCaptchaGID(loginResponse.getCaptchaGID());
            return result;
        }

        if (loginResponse.isEmailAuthNeeded()) {
        	result = new LoginResult(LoginResultState.NEED_EMAIL);
        	result.setRequiresEmail(true);
        	result.setSteamID(Long.toString(loginResponse.getEmailSteamID()));
            return result;
        }

        if (loginResponse.isTwoFactorNeeded() && !loginResponse.isSuccess()) {
        	result = new LoginResult(LoginResultState.NEED_2FA);
        	result.setRequires2FA(true);
            return result;
        }

        if (loginResponse.getMessage() != null && loginResponse.getMessage().contains("too many login failures")) {
            return LoginResult.TOO_MANY_FAILED_LOGINS;
        }

        if (loginResponse.getOAuthData() == null || loginResponse.getOAuthData().getOAuthToken() == null || loginResponse.getOAuthData().getOAuthToken().length() == 0) {
            return LoginResult.GENERAL_FAILURE;
        }

        if (!loginResponse.isLoginComplete()) {
            return LoginResult.BAD_CREDENTIALS;
        } else {
        	result = new LoginResult(LoginResultState.LOGIN_OK);
        	LoginResponse.OAuth oAuthData = loginResponse.getOAuthData();

            SessionData session = new SessionData();
            session.setOAuthToken(oAuthData.getOAuthToken());
            session.setSteamID(oAuthData.getSteamID());
            session.setSteamLogin(session.getSteamID() + "%7C%7C" + oAuthData.getSteamLogin());
            session.setSteamLoginSecure(session.getSteamID() + "%7C%7C" + oAuthData.getSteamLoginSecure());
            session.setWebCookie(oAuthData.getWebcookie());
            session.setSessionID(_cookies.get("sessionid").value());
            result.setSession(session);
            result.setLoggedIn(true);
            return result;
        }
}

	public String getSecurePassword(String password, String modulusHex, String exponentHex) throws UnsupportedEncodingException {
		String encryptedPassword = "";
		byte[] passwordBytes = password.getBytes("ASCII");        
        try {
        	BigInteger modulus = new BigInteger(modulusHex, 16);
        	BigInteger exponent = new BigInteger(exponentHex, 16);
        			
        	KeyFactory factory = KeyFactory.getInstance("RSA");
        	RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
        	PublicKey pub = factory.generatePublic(spec);
        	
        	Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        	//Cipher cipher = Cipher.getInstance("RSA");
        	cipher.init(Cipher.ENCRYPT_MODE, pub);

        	byte[] bytes = cipher.doFinal(passwordBytes);
			encryptedPassword = Base64.encodeBase64String(bytes);
    	} catch(Exception e) {
    	}
		return encryptedPassword;
	}
}
