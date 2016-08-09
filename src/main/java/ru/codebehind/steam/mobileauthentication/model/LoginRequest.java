package ru.codebehind.steam.mobileauthentication.model;

public class LoginRequest {
    private String Username;
    private String Password;
    private long SteamID;
    private boolean RequiresCaptcha;
    private String CaptchaGID = null;
    private String CaptchaText = null;
    private boolean RequiresEmail;
    private String EmailDomain = null;
    private String EmailCode = null;
    private boolean Requires2FA;
    private String TwoFactorCode = null;
    private SessionData Session = null;
    private boolean LoggedIn = false;
}
