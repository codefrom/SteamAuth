package ru.codebehind.steam.mobileauthentication.model;

public enum LoginResultState {
	NONE,
    LOGIN_OK,
    GENERAL_FAILURE,
    BAD_RSA,
    BAD_CREDENTIALS,
    NEED_CAPTCHA,
    NEED_2FA,
    NEED_EMAIL,
    TOO_MANY_FAILED_LOGINS,
}
