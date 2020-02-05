
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

/**
 * The listener that listens to request from users in the server
 */
public class MessageListener extends ListenerAdapter {

    /**
     * The Regex that represents YouTube links
     */
    private final String REGEX = "http(?:s?):\\/\\/(?:www\\.)?youtu(?:be\\.com\\/watch\\?v=|\\.be\\/)([\\w\\-\\_]*)(&(amp;)?‌​[\\w\\?‌​=]*)?";

    /**
     * The SettingsManager that holds all relevant values and should be updated on shutdown
     */
    private final SettingsManager settings;

    /**
     * The Pattern that is used to match the Discord lines against
     */
    private final Pattern pattern;

    /**
     * Create a listener based on the settings
     *
     * @param settings The initialized SettingsManager that contains all the relevant values
     */
    public MessageListener(SettingsManager settings) {
        this.settings = settings;
        this.pattern = Pattern.compile(REGEX);
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
            matchLinks(event.getMessage().getContentDisplay().split(" "));
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

    /**
     * Match an array of strings with the given pattern
     * @param arr The strings the pattern should be matched against
     */
    private void matchLinks(String[] arr) {
        for (String s : arr) {
            if (pattern.matcher(s).matches()) {
                System.out.println(s);
            }
        }
    }

    public processHistory(MessageHistory history){

    }
}
