import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.regex.Pattern;

/**
 * Class to handle all the YouTube API calls
 */
public class YoutubeHandler {

    /**
     * The Regex that represents YouTube links
     */
    private final String REGEX = "http(?:s?):\\/\\/(?:www\\.)?youtu(?:be\\.com\\/watch\\?v=|\\.be\\/)([\\w\\-\\_]*)(&(amp;)?‌​[\\w\\?‌​=]*)?";

    /**
     * The Pattern that is used to match the Discord lines against
     */
    private final Pattern pattern;

    /**
     * Create a new YoutubeHandler
     */
    public YoutubeHandler(){
        this.pattern = Pattern.compile(REGEX);
    }

    /**
     * Match a message with the YouTube Pattern and do an action afterwards
     * @param message The message that should be matched against
     */
    public void matchLinks(Message message) {
        for (String s : message.getContentDisplay().split(" ")) {
            if (pattern.matcher(s).matches()) {
                System.out.println(s);
            }
        }
    }

    /**
     * Process the message history the bot has missed
     * @param channel The channel the history should be retrieved from
     * @param settings The settings that should be updated and read for the lastMessageID
     */
    public void processHistory(TextChannel channel, SettingsManager settings){
        if(settings.getLastMessageID()==null){
            channel.getHistoryFromBeginning(1).queue(history -> {
                if (!history.isEmpty()) {
                    Message first = history.getRetrievedHistory().get(0);
                    matchLinks(first);
                    settings.setLastMessageID(first.getIdLong());
                    processHistory(channel,settings);
                } else{
                    return;
                }
            });
        } else {
            channel.getHistoryAfter(settings.getLastMessageID(), 50).queue(history -> {
                if (!history.isEmpty()) {
                    Message first = history.getRetrievedHistory().get(0);
                    settings.setLastMessageID(first.getIdLong());
                    for(Message m : history.getRetrievedHistory()){
                        matchLinks(m);
                    }
                    processHistory(channel,settings);
                }
            });
        }
    }
}
