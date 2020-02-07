package playlistbot;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import playlistbot.youtube.YoutubeHandler;

import javax.annotation.Nonnull;

/**
 * The listener that listens to request from users in the server
 */
public class MessageListener extends ListenerAdapter {

    /**
     * The SettingsManager that holds all relevant values and should be updated on shutdown
     */
    private final SettingsManager settings;

    /**
     * The YouTube Handler that should process YouTube link messages
     */
    private final YoutubeHandler ytHandler;

    /**
     * Create a listener based on the settings
     *
     * @param settings The initialized playlistbot.SettingsManager that contains all the relevant values
     * @param ytHandler The ytHandler that should be called on new messages
     */
    public MessageListener(SettingsManager settings, YoutubeHandler ytHandler) {
        this.settings = settings;
        this.ytHandler = ytHandler;
    }

    /**
     * Report when the bot is connected and ready
     *
     * @param event Unused ready event.
     */
    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("Bot connection ready to go");
    }

    /**
     * React to messages from the selected channel
     * @param event The message event that is being called
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getChannel().getIdLong() == settings.getChannelID()) {
            ytHandler.matchLinks(event.getMessage());
        }
        settings.setLastMessageID(event.getMessageIdLong());
    }

    /**
     * Save values when the bot is shutting down
     * @param event The shutdown event
     */
    @Override
    public void onShutdown(@Nonnull ShutdownEvent event) {
        System.out.println("Bot terminating...");
        if(settings.writeValues()){
            System.out.println("Saving was successful");
        } else {
            System.out.println("Settings file might be corrupted. Delete it or manually correct it.");
        }
    }
}
