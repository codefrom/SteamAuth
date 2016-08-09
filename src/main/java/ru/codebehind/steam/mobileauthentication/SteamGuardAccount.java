package ru.codebehind.steam.mobileauthentication;

public class SteamGuardAccount {
    public static class WGTokenInvalidException extends Exception
    {
		private static final long serialVersionUID = 2816075243720326723L;
    }

    public static class WGTokenExpiredException extends Exception
    {
		private static final long serialVersionUID = 111194300708646219L;
    }
}
