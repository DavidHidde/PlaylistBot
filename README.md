# PlaylistBot
PlaylistBot is a JDA based Discord bot that gathers YouTube links in a Discord Channel and puts the videos into a Yotube Playlist. It doesn't add duplicate videos and always puts new videos on top. It also processes chat history so it doesn't miss messages when it has down time. The bot uses the JDA Discord API wrapper along with the Google YouTube and OAuth Java API wrappers.

## Installation

To install this bot you need a couple of things:
 - The [jar](https://github.com/DavidHidde/PlaylistBot/releases) of the bot
 - An installation of Java
 - An API key for the YouTube Data API, complete with OAuth 2 screen
 - A Discord bot token
 - A YouTube playlist
 - A Discord channel to link the bot to
 
 I won't go over how to add the bot to your Discord server. This is already documented by a lot of other people so I'll just leave this [here](https://discordpy.readthedocs.io/en/latest/discord.html).
 
 ### YouTube API key retrieval and other setup
 
To get the bot working with YouTube, you need access to the API. To do this, you need to make a new project in the Google developer console. You then proceed to make a basic OAuth screen for it for external use for which you only have to add the '/auth/youtube' domain. The system will respond with a pop-up about the screen needing to be verified, but as long as only you are using the key it's fine. You then only have to create a new OAuth client ID, download the secrets, rename the secrets to 'client_secrets' and you're good to go!

When running the bot for the first time, it will prompt for the Discord token, the ID of the channel it should listen to and the ID of the playlist it should inject new videos into. These settings are stored afterwards and don't have to be entered again.

## Considerations

For now, the bot only listens to 1 channel on 1 server and doesn't ever respond back. This is fine for my own purposes, but it might not be for you. If you want to adjust the bot to your liking using Java, you have to keep in mind that some of the dependencies used are signed, so you have to manually remove the signed files from the META-INF after compilation.
