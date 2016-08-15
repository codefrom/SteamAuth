# SteamAuth
Steam Mobile Guard authenticator, written in Java

# Credits
Idea and algorithms taken from geel9/SteamAuth

# Features
Currently this library can:
* Emulate installed steam mobile authenticator for android (with authenticator working on the phone)
* Login to a user account
* Generate login codes for a given Shared Secret
* Fetch, accept, and deny mobile confirmations

## Far far away features (maybe some day)
* Link and activate a new mobile authenticator to a user account after logging in
* Remove itself from an account

# Requirements
This library using:
* JUnit
* FasterXML Jackson
* OkHTTP
* Apache Commons - codec

# Usage
No time to explain :)
Some usage examplex:
## Accept/deny confirmation
1. Create SteamGuardAccount with appropriate config
1. Call UserLoginService.DoLogin with username and password
   1. If LoginResult.State = LoginResultState.NEED_2FA then call UserLoginService.DoLogin, but now set Requires2FA to true and TwoFactorCode to SteamGuardAccount.GenerateSteamGuardCode value for request
   1. If LoginResult.State = LoginResultState.NEED_CAPTCHA then call UserLoginService.DoLogin, but now set RequiresCaptcha to true, CaptchaGID to CaptchaGID from response and CaptchaText to text from CAPTCHA at "https://steamcommunity.com/public/captcha.php?gid=" + CaptchaGID
1. Set received session to the SteamGuardAccount
1. Call SteamGuardAccount.FetchConfirmations to get array of confirmations
   1. Call SteamGuardAccount.AcceptConfirmation to accept confirmation
   1. Call SteamGuardAccount.DenyConfirmation to deny confirmation

# Steam Mobile Reverse
Already some one made guide here - https://github.com/hyt47/SteamDesktopAuthenticator-Mod-47/wiki/Manually-import-your-Steam-Account-from-Android

P.S. You'll need "Steamguard-[SteamID]" file in /data/data/com.valvesoftware.android.steam.community/files