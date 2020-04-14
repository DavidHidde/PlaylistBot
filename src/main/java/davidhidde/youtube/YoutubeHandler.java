package davidhidde.youtube;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import davidhidde.SettingsManager;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to handle matching the YouTube links and calling the YoutubeRequester
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
     * The requester that should e called to insert playlist items
     */
    private YoutubeRequester requester;

    /**
     * The manager this handler should pull data from
     */
    private SettingsManager settings;

    /**
     * Create a new YoutubeHandler
     */
    public YoutubeHandler(SettingsManager settings){
        this.settings = settings;
        this.requester = new YoutubeRequester();
        this.pattern = Pattern.compile(REGEX);
    }

    /**
     * Match a message with the YouTube Pattern and do an action afterwards
     * @param message The message that should be matched against
     */
    public void matchLinks(Message message) {
        for (String s : message.getContentDisplay().split(" ")) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.matches()) {
                System.out.println(s + " found");
                requester.addPlaylistItem(matcher.group(1), settings.getPlaylistID());
            }
        }
    }

    /**
     * Process the message history the bot has missed
     * @param channel The channel the history should be retrieved from
     */
    public void processHistory(TextChannel channel){
        if(settings.getLastMessageID()==null){
            channel.getHistoryFromBeginning(1).queue(history -> {
                if (!history.isEmpty()) {
                    Message first = history.getRetrievedHistory().get(0);
                    matchLinks(first);
                    settings.setLastMessageID(first.getIdLong());
                    processHistory(channel);
                }
            });
        } else {
            channel.getHistoryAfter(settings.getLastMessageID(), 50).queue(history -> {
                if (!history.isEmpty()) {
                    List<Message> list = history.getRetrievedHistory();
                    for(int i = list.size()-1; i>=0; i--){
                        matchLinks(list.get(i));
                    }
                    settings.setLastMessageID(list.get(0).getIdLong());
                    processHistory(channel);
                }
            });
        }
    }
}
