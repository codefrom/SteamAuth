package ru.codebehind.steam.mobileauthentication.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class LoginRequest {
    private String username = "";
    private String password = "";
    private long steamID;
    private boolean requiresCaptcha;
    private boolean requiresEmail;
    private boolean requires2FA;
    private String captchaGID = "";
    private String captchaText = "";
    private String emailDomain = "";
    private String emailCode = "";
    private String twoFactorCode = "";
    
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public long getSteamID() {
		return steamID;
	}
	public void setSteamID(long steamID) {
		this.steamID = steamID;
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
	public String getCaptchaText() {
		return captchaText;
	}
	public void setCaptchaText(String captchaText) {
		this.captchaText = captchaText;
	}
	public boolean isRequiresEmail() {
		return requiresEmail;
	}
	public void setRequiresEmail(boolean requiresEmail) {
		this.requiresEmail = requiresEmail;
	}
	public String getEmailDomain() {
		return emailDomain;
	}
	public void setEmailDomain(String emailDomain) {
		this.emailDomain = emailDomain;
	}
	public String getEmailCode() {
		return emailCode;
	}
	public void setEmailCode(String emailCode) {
		this.emailCode = emailCode;
	}
	public boolean isRequires2FA() {
		return requires2FA;
	}
	public void setRequires2FA(boolean requires2fa) {
		requires2FA = requires2fa;
	}
	public String getTwoFactorCode() {
		return twoFactorCode;
	}
	public void setTwoFactorCode(String twoFactorCode) {
		this.twoFactorCode = twoFactorCode;
	}
}
