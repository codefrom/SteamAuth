# SteamAuth
Steam Mobile Guard authenticator, written in Java

# Credits
Idea and algorithms taken from geel9/SteamAuth

# Functionality
Currently this library can:
* Login to a user account

# Requirements

# Usage

# Upcoming Features
(in priority order)
* Emulate installed steam mobile authenticator for android (with authenticator working on the phone)
* Fetch, accept, and deny mobile confirmations

## Far far away
* Generate login codes for a given Shared Secret
* Link and activate a new mobile authenticator to a user account after logging in
* Remove itself from an account

# Steam Mobile Reverse
Needed params:
* identity_secret - in "Steamguard-[SteamID]" file in /data/data/com.valvesoftware.android.steam.community/files
* deviceid - in "steam.uuid.xml"

EDIT: Already some guide here - https://github.com/hyt47/SteamDesktopAuthenticator-Mod-47/wiki/Manually-import-your-Steam-Account-from-Android