package ru.codebehind.steam.mobileauthentication;

import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import okhttp3.HttpUrl;
import ru.codebehind.okhttp.NameValuePairList;
import ru.codebehind.okhttp.cookies.SimpleCookieJar;
import ru.codebehind.steam.mobileauthentication.model.Confirmation;
import ru.codebehind.steam.mobileauthentication.model.SessionData;
import ru.codebehind.toolbelt.JsonHelper;

public class SteamGuardAccount {
    @JsonIgnoreProperties(ignoreUnknown=true)
	public static class RefreshSessionDataResponse {
        @JsonIgnoreProperties(ignoreUnknown=true)
        public static class RefreshSessionDataInternalResponse
        {
            @JsonProperty(value="token")
            private String token;

            @JsonProperty(value="token_secure")
            private String tokenSecure;

			public String getToken() {
				return token;
			}

			public void setToken(String token) {
				this.token = token;
			}

			public String getTokenSecure() {
				return tokenSecure;
			}

			public void setTokenSecure(String tokenSecure) {
				this.tokenSecure = tokenSecure;
			}
        }

        @JsonProperty(value="response")
        private RefreshSessionDataInternalResponse response;

		public RefreshSessionDataInternalResponse getResponse() {
			return response;
		}

		public void setResponse(RefreshSessionDataInternalResponse response) {
			this.response = response;
		}        
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class RemoveAuthenticatorResponse
    {
        @JsonIgnoreProperties(ignoreUnknown=true)
        public static class RemoveAuthenticatorInternalResponse
        {
            @JsonProperty(value="success")
            private boolean success;

			public boolean isSuccess() {
				return success;
			}

			public void setSuccess(boolean success) {
				this.success = success;
			}            
        }

        @JsonProperty(value="response")
        private RemoveAuthenticatorInternalResponse response;

		public RemoveAuthenticatorInternalResponse getResponse() {
			return response;
		}

		public void setResponse(RemoveAuthenticatorInternalResponse response) {
			this.response = response;
		}
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class SendConfirmationResponse
    {
        @JsonProperty(value="success")
        private boolean success;

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}        
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class ConfirmationDetailsResponse
    {
        @JsonProperty(value="success")
        private boolean success;

        @JsonProperty(value="html")
        private String html;

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public String getHtml() {
			return html;
		}

		public void setHtml(String html) {
			this.html = html;
		}        
    }
    
    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class Config {
        @JsonProperty(value="shared_secret")
        private String sharedSecret;

        @JsonProperty(value="serial_number")
        private String serialNumber;

        @JsonProperty(value="revocation_code")
        private String revocationCode;

        @JsonProperty(value="uri")
        private String uri;

        @JsonProperty(value="server_time")
        private long serverTime;

        @JsonProperty(value="account_name")
        private String accountName;

        @JsonProperty(value="token_gid")
        private String tokenGID;

        @JsonProperty(value="identity_secret")
        private String identitySecret;

        @JsonProperty(value="secret_1")
        private String secret1;

        @JsonProperty(value="status")
        private int status;

        @JsonProperty(value="device_id")
        private String deviceID;

        @JsonProperty(value="fully_enrolled")
	    private boolean fullyEnrolled;

		public String getSharedSecret() {
			return sharedSecret;
		}

		public void setSharedSecret(String sharedSecret) {
			this.sharedSecret = sharedSecret;
		}

		public String getSerialNumber() {
			return serialNumber;
		}

		public void setSerialNumber(String serialNumber) {
			this.serialNumber = serialNumber;
		}

		public String getRevocationCode() {
			return revocationCode;
		}

		public void setRevocationCode(String revocationCode) {
			this.revocationCode = revocationCode;
		}

		public String getUri() {
			return uri;
		}

		public void setUri(String uri) {
			this.uri = uri;
		}

		public long getServerTime() {
			return serverTime;
		}

		public void setServerTime(long serverTime) {
			this.serverTime = serverTime;
		}

		public String getAccountName() {
			return accountName;
		}

		public void setAccountName(String accountName) {
			this.accountName = accountName;
		}

		public String getTokenGID() {
			return tokenGID;
		}

		public void setTokenGID(String tokenGID) {
			this.tokenGID = tokenGID;
		}

		public String getIdentitySecret() {
			return identitySecret;
		}

		public void setIdentitySecret(String identitySecret) {
			this.identitySecret = identitySecret;
		}

		public String getSecret1() {
			return secret1;
		}

		public void setSecret1(String secret1) {
			this.secret1 = secret1;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getDeviceID() {
			return deviceID;
		}

		public void setDeviceID(String deviceID) {
			this.deviceID = deviceID;
		}

		public boolean isFullyEnrolled() {
			return fullyEnrolled;
		}

		public void setFullyEnrolled(boolean fullyEnrolled) {
			this.fullyEnrolled = fullyEnrolled;
		}		
	}
	
    public static class WGTokenInvalidException extends Exception
    {
		private static final long serialVersionUID = 2816075243720326723L;
    }

    public static class WGTokenExpiredException extends Exception
    {
		private static final long serialVersionUID = 111194300708646219L;
    }
    
    public SessionData session;
	
    public SessionData getSession() {
		return session;
	}

	public void setSession(SessionData session) {
		this.session = session;
	}

	final ThreadFactory threadFactory = new ThreadFactoryBuilder()
	        .setNameFormat("SteamGuardAccount-%d")
	        .setDaemon(true)
	        .build();
	private ExecutorService executor = Executors.newCachedThreadPool(threadFactory);
	private Config config;

    private final static byte[] STEAM_GUARD_CODE_TRANSLATIONS = new byte[] { 50, 51, 52, 53, 54, 55, 56, 57, 66, 67, 68, 70, 71, 72, 74, 75, 77, 78, 80, 81, 82, 84, 86, 87, 88, 89 };

    public SteamGuardAccount(Config config) {
    	this.setConfig(config);
    }
    
    public Confirmation[] FetchConfirmations() throws Throwable {
        String url = GenerateConfirmationURL();

        SimpleCookieJar cookies = new SimpleCookieJar();
        this.session.addCookies(cookies);

        String response = SteamWeb.DoRequest(url, "GET", null, null, null, cookies, null);

        /*So you're going to see this abomination and you're going to be upset.
          It's understandable. But the thing is, regex for HTML -- while awful -- makes this way faster than parsing a DOM, plus we don't need another library.
          And because the data is always in the same place and same format... It's not as if we're trying to naturally understand HTML here. Just extract strings.
          I'm sorry. */

        Pattern confIDRegex = Pattern.compile("data-confid=\"(\\d+)\"");
        Pattern confKeyRegex = Pattern.compile("data-key=\"(\\d+)\"");
        Pattern confDescRegex = Pattern.compile("<div>((Confirm|Trade with|Sell -) .+)</div>");
        
        Matcher confIDs = confIDRegex.matcher(response);
        Matcher confKeys = confKeyRegex.matcher(response);
        Matcher confDescs = confDescRegex.matcher(response);

        if (response == null || !(confIDs.find() && confKeys.find() && confDescs.find())) {
            if (response == null || !response.contains("<div>Nothing to confirm</div>")) {
                throw new WGTokenInvalidException();
            }
            return new Confirmation[0];
        }


        ArrayList<Confirmation> ret = new ArrayList<Confirmation>();
    	do {
            String confID = confIDs.group(1);
            String confKey = confKeys.group(1);
            String confDesc = confDescs.group(1);
            Confirmation conf = new Confirmation();
    		conf.setDescription(confDesc);
            conf.setID(confID);
            conf.setKey(confKey);
            ret.add(conf);
    	} while(confIDs.find() && confKeys.find() && confDescs.find());


        return ret.toArray(new Confirmation[0]);
    }

    public Future<Confirmation[]> FetchConfirmationsAsync() {
    	return executor.submit(new Callable<Confirmation[]>() {
			public Confirmation[] call() throws Exception {
				try {
					return FetchConfirmations();
				} catch (Throwable e) {
					e.printStackTrace();
				}
				return new Confirmation[0];
			}
		});
	}

/*    public long GetConfirmationTradeOfferID(Confirmation conf)
    {
        var confDetails = _getConfirmationDetails(conf);
        if (confDetails == null || !confDetails.Success) return -1;

        Regex tradeOfferIDRegex = new Regex("<div class=\"tradeoffer\" id=\"tradeofferid_(\\d+)\" >");
        if (!tradeOfferIDRegex.IsMatch(confDetails.HTML)) return -1;
        return long.Parse(tradeOfferIDRegex.Match(confDetails.HTML).Groups[1].Value);
    }*/

    /*public boolean AcceptMultipleConfirmations(Confirmation[] confs)
    {
        return _sendMultiConfirmationAjax(confs, "allow");
    }

    public boolean DenyMultipleConfirmations(Confirmation[] confs)
    {
        return _sendMultiConfirmationAjax(confs, "cancel");
    }*/

    public boolean AcceptConfirmation(Confirmation conf) throws Throwable {
        return _sendConfirmationAjax(conf, "allow");
    }

    public boolean DenyConfirmation(Confirmation conf) throws Throwable {
        return _sendConfirmationAjax(conf, "cancel");
    }

    /// <summary>
    /// Refreshes the Steam session. Necessary to perform confirmations if your session has expired or changed.
    /// </summary>
    /// <returns></returns>
    public boolean RefreshSession() throws Throwable {
        String url = APIEndpoints.MOBILEAUTH_GETWGTOKEN;
        String response = null;
        try  {
        	NameValuePairList postData = new NameValuePairList();
        	postData.add("access_token", session.getOAuthToken());
            response = SteamWeb.DoRequest(url, "POST", postData, null, null, null, null);
        } catch (Exception e) {
            return false;
        }

        if (response == null)
        	return false;

        try {
        	RefreshSessionDataResponse refreshResponse = JsonHelper.Deserialize(RefreshSessionDataResponse.class, response);
            if (refreshResponse == null || 
        		refreshResponse.getResponse() == null || 
        		refreshResponse.getResponse().getToken() == null || 
        		refreshResponse.getResponse().getToken().equals(""))
                return false;

            String token = session.getSteamID() + "%7C%7C" + refreshResponse.getResponse().getToken();
            String tokenSecure = session.getSteamID() + "%7C%7C" + refreshResponse.getResponse().getTokenSecure();

            session.setSteamLogin(token);
            session.setSteamLoginSecure(tokenSecure);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /// <summary>
    /// Refreshes the Steam session. Necessary to perform confirmations if your session has expired or changed.
    /// </summary>
    /// <returns></returns>
    public Future<Boolean> RefreshSessionAsync() {
    	return executor.submit(new Callable<Boolean>() {
    		public Boolean call() throws Exception {
				try {
					return RefreshSession();
				} catch (Throwable e) {
					e.printStackTrace();
				}
				return false;
			}
		});
    }

/*    private ConfirmationDetailsResponse _getConfirmationDetails(Confirmation conf)
    {
        string url = APIEndpoints.COMMUNITY_BASE + "/mobileconf/details/" + conf.ID + "?";
        string queryString = GenerateConfirmationQueryParams("details");
        url += queryString;

        CookieContainer cookies = new CookieContainer();
        this.Session.AddCookies(cookies);
        string referer = GenerateConfirmationURL();

        string response = SteamWeb.Request(url, "GET", "", cookies, null);
        if (String.IsNullOrEmpty(response)) return null;

        var confResponse = JsonConvert.DeserializeObject<ConfirmationDetailsResponse>(response);
        if (confResponse == null) return null;
        return confResponse;
    }*/

    private boolean _sendConfirmationAjax(Confirmation conf, String op) throws Throwable {
        String url = APIEndpoints.COMMUNITY_BASE + "/mobileconf/ajaxop";
        NameValuePairList query = new NameValuePairList();
        query.add("op", op);
        query.addAll(GenerateConfirmationQueryParams(op));
        query.add("cid", conf.getID());
        query.add("ck", conf.getKey());

        SimpleCookieJar cookies = new SimpleCookieJar();
        session.addCookies(cookies);
        String referer = GenerateConfirmationURL();

        String response = SteamWeb.DoRequest(url, "GET", null, null, query, cookies, referer);
        if (response == null)
        	return false;

        SendConfirmationResponse confResponse = JsonHelper.Deserialize(SendConfirmationResponse.class, response);
        return confResponse.isSuccess();
    }

    /*private boolean _sendMultiConfirmationAjax(Confirmation[] confs, String op)
    {
        String url = APIEndpoints.COMMUNITY_BASE + "/mobileconf/multiajaxop";

        string query = "op=" + op + "&" + GenerateConfirmationQueryParams(op);
        foreach (var conf in confs)
        {
            query += "&cid[]=" + conf.ID + "&ck[]=" + conf.Key;
        }

        CookieContainer cookies = new CookieContainer();
        this.Session.AddCookies(cookies);
        string referer = GenerateConfirmationURL();

        string response = SteamWeb.Request(url, "POST", query, cookies, null);
        if (response == null) return false;

        SendConfirmationResponse confResponse = JsonConvert.DeserializeObject<SendConfirmationResponse>(response);
        return confResponse.Success;
    }*/

    public String GenerateConfirmationURL() throws Throwable  {
    	return GenerateConfirmationURL("conf");
    }
    public String GenerateConfirmationURL(String tag) throws Throwable  {
    	HttpUrl.Builder urlBuilder = HttpUrl.get(URI.create(APIEndpoints.COMMUNITY_BASE + "/mobileconf/conf")).newBuilder();
        NameValuePairList query = GenerateConfirmationQueryParams(tag);
        for (NameValuePairList.NameValuePair nameValuePair : query) {
        	urlBuilder.addQueryParameter(nameValuePair.getName(), nameValuePair.getValue());
		}
        return urlBuilder.build().toString();
    }

    public NameValuePairList GenerateConfirmationQueryParams(String tag) throws Throwable {
        if (getConfig().getDeviceID() == null ||
        	getConfig().getDeviceID().equals(""))
            throw new Exception("Device ID is not present");

        long time = TimeAligner.GetSteamTime();

        NameValuePairList ret = new NameValuePairList();
        ret.add("p", getConfig().getDeviceID());
        ret.add("a", Long.toString(session.getSteamID()));
        ret.add("k", _generateConfirmationHashForTime(time, tag));
        ret.add("t", Long.toString(time));
        ret.add("m", "android");
        ret.add("tag", tag);

        return ret;
    }

    private String _generateConfirmationHashForTime(long time, String tag) throws Throwable {
        byte[] decode = Base64.decodeBase64(getConfig().getIdentitySecret());
        int n2 = 8;
        if (tag != null)
        {
            if (tag.length() > 32)
            {
                n2 = 8 + 32;
            }
            else
            {
                n2 = 8 + tag.length();
            }
        }
        byte[] array = new byte[n2];
        int n3 = 8;
        while (true)
        {
            int n4 = n3 - 1;
            if (n3 <= 0)
            {
                break;
            }
            array[n4] = (byte)time;
            time >>= 8;
            n3 = n4;
        }
        if (tag != null) {
        	System.arraycopy(tag.getBytes("UTF8"), 0, array, 8, n2 - 8); 
        }

        try {
        	SecretKeySpec signingKey = new SecretKeySpec(decode, "HmacSHA1");
    		Mac mac = Mac.getInstance("HmacSHA1");
    		mac.init(signingKey);
    		byte[] hashedData = mac.doFinal(array);

    		String encodedData = Base64.encodeBase64String(hashedData);
    		//String hash = URLEncoder.encode(encodedData, "UTF8");
            return encodedData;
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
    }

    public String GenerateSteamGuardCode() throws Throwable {
        return GenerateSteamGuardCodeForTime(TimeAligner.GetSteamTime());
    }

    public String GenerateSteamGuardCodeForTime(long time) {
        if (config.getSharedSecret() == null || config.getSharedSecret().length() == 0) {
            return "";
        }

        byte[] sharedSecretArray = Base64.decodeBase64(config.getSharedSecret());
        byte[] timeArray = new byte[8];

        time /= 30L;

        for (int i = 8; i > 0; i--) {
            timeArray[i - 1] = (byte)time;
            time >>= 8;
        }
        
        try {
        	SecretKeySpec signingKey = new SecretKeySpec(sharedSecretArray, "HmacSHA1");
    		Mac mac = Mac.getInstance("HmacSHA1");
    		mac.init(signingKey);
    		byte[] hashedData = mac.doFinal(timeArray);
            byte[] codeArray = new byte[5];
            byte b = (byte)(hashedData[19] & 0xF);
            int codePoint = (hashedData[b] & 0x7F) << 24 | (hashedData[b + 1] & 0xFF) << 16 | (hashedData[b + 2] & 0xFF) << 8 | (hashedData[b + 3] & 0xFF);

            for (int i = 0; i < 5; ++i) {
                codeArray[i] = STEAM_GUARD_CODE_TRANSLATIONS[codePoint % STEAM_GUARD_CODE_TRANSLATIONS.length];
                codePoint /= STEAM_GUARD_CODE_TRANSLATIONS.length;
            }
            
            return new String(codeArray, "UTF-8");
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }        
    }
    
	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}
}
