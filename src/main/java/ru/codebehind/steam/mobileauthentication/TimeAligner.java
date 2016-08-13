package ru.codebehind.steam.mobileauthentication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import ru.codebehind.okhttp.NameValuePairList;
import ru.codebehind.toolbelt.DateHelper;
import ru.codebehind.toolbelt.JsonHelper;

/// <summary>
/// Class to help align system time with the Steam server time. Not super advanced; probably not taking some things into account that it should.
/// Necessary to generate up-to-date codes. In general, this will have an error of less than a second, assuming Steam is operational.
/// </summary
public class TimeAligner {
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class TimeQuery {
		@JsonIgnoreProperties(ignoreUnknown=true)
        public static class TimeQueryResponse {
            @JsonProperty(value="server_time")
            private long serverTime;

			public long getServerTime() {
				return serverTime;
			}

			public void setServerTime(long serverTime) {
				this.serverTime = serverTime;
			}            
        }

        @JsonProperty(value="response")
        private TimeQueryResponse response;

		public TimeQueryResponse getResponse() {
			return response;
		}

		public void setResponse(TimeQueryResponse response) {
			this.response = response;
		}       
    }
	
    private static boolean _aligned = false;
    private static int _timeDifference = 0;

    public static long GetSteamTime() throws Throwable {
        if (!TimeAligner._aligned) {
            TimeAligner.AlignTime();
        }
        return DateHelper.GetSystemUnixTime() + _timeDifference;
    }

    public static void AlignTime() throws Throwable {
        long currentTime = DateHelper.GetSystemUnixTime();
        try {
        	NameValuePairList postData = new NameValuePairList();
        	postData.add("steamid", "0");
        	String response = SteamWeb.DoRequest(APIEndpoints.TWO_FACTOR_TIME_QUERY, "POST", postData, null, null, null, null);
            TimeQuery query = JsonHelper.Deserialize(TimeQuery.class, response);
            TimeAligner._timeDifference = (int)(query.getResponse().getServerTime() - currentTime);
            TimeAligner._aligned = true;
        } catch (Exception e) {
        	e.printStackTrace();
            return;
        }
    }
}
