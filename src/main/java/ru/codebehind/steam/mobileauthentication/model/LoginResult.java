package ru.codebehind.steam.mobileauthentication.model;

public class LoginResult {
	public static final LoginResult LOGIN_OK = new LoginResult(LoginResultState.LOGIN_OK);
	public static final LoginResult GENERAL_FAILURE = new LoginResult(LoginResultState.GENERAL_FAILURE);
	public static final LoginResult BAD_RSA = new LoginResult(LoginResultState.BAD_RSA);
	public static final LoginResult BAD_CREDENTIALS = new LoginResult(LoginResultState.BAD_CREDENTIALS);
	public static final LoginResult TOO_MANY_FAILED_LOGINS = new LoginResult(LoginResultState.TOO_MANY_FAILED_LOGINS);

	private LoginResultState state;
	private boolean requiresCaptcha;
	private boolean requiresEmail;
	private boolean requires2FA;
	private String captchaGID;
	private String steamID;
	private SessionData session;
	private boolean loggedIn = false;
	
	public LoginResult() {
		this.state = LoginResultState.NONE;
	}
	
	public LoginResult(LoginResultState state) {
		this.state = state;
	}

	public LoginResultState getState() {
		return state;
	}

	public void setState(LoginResultState state) {
		this.state = state;
	}

	public boolean isRequiresCaptcha() {
		return requiresCaptcha;
	}

	public void setRequiresCaptcha(boolean requiresCaptcha) {
		this.requiresCaptcha = requiresCaptcha;
	}

	public String getCaptchaGID() {
		return captchaGID;
	}

	public void setCaptchaGID(String captchaGID) {
		this.captchaGID = captchaGID;
	}

	public boolean isRequiresEmail() {
		return requiresEmail;
	}

	public void setRequiresEmail(boolean requiresEmail) {
		this.requiresEmail = requiresEmail;
	}

	public String getSteamID() {
		return steamID;
	}

	public void setSteamID(String steamID) {
		this.steamID = steamID;
	}

	public boolean isRequires2FA() {
		return requires2FA;
	}

	public void setRequires2FA(boolean requires2fa) {
		requires2FA = requires2fa;
	}

	public SessionData getSession() {
		return session;
	}

	public void setSession(SessionData session) {
		this.session = session;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
}
