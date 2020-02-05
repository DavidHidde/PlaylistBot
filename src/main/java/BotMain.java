import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import javax.security.auth.login.LoginException;
import java.util.EnumSet;
import java.util.EventListener;

/**
 * Main class of the bot. Sets preferences, connects and starts the bot.
 */
public class BotMain implements EventListener{

    private static JDA setupJDA(SettingsManager settings) throws LoginException{
        System.out.println("Setting up bot...");
        JDABuilder builder = new JDABuilder(settings.getToken());
        builder.setDisabledCacheFlags(EnumSet.of(CacheFlag.EMOTE, CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE, CacheFlag.CLIENT_STATUS));
        builder.setActivity(Activity.listening("to old memes I missed"));
        builder.addEventListeners(new MessageListener(settings.getChannelID()));
        return builder.build();
    }

    /**
     * Main method of the bot
     * @param args Unused args strings, can be used i.e. for token.
     */
    public static void main(String[] args) throws LoginException{
        System.out.println("Initializing bot values...");
        SettingsManager settings = new SettingsManager();
        if(settings.isCorrect()){
            JDA jda = setupJDA(settings);

        } else {
            System.out.println("Error occurred while reading settings file, aborting...");
        }
    }
}
