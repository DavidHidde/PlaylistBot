import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * The listener that listens to request from users in the server
 */
public class MessageListener extends ListenerAdapter {

    /**
     * The ID of the channel this listener should listen to
     */
    private final long channelID;

    /**
     * Create a listener for a channel
     * @param channelID The ID of the channel that should be listened to
     */
    public MessageListener(long channelID){
        super();
        this.channelID = channelID;
    }

    /**
     * Report when the bot is connected and ready
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
        if (event.getChannel().getIdLong() == channelID) {
            String[] content = event.getMessage().getContentDisplay().split("youtube.com/");
        }
    }
}
