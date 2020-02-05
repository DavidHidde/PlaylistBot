import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import okhttp3.OkHttpClient;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;
import java.util.Scanner;

/**
 * Main class of the bot. Sets preferences, connects and starts the bot.
 */
public class BotMain{

    /**
     * Initialize the values for the JDA
     * @param settings The settings used
     * @return The JDA that has been build
     * @throws LoginException Thrown when the token is invalid or something else went wrong
     */
    private static JDA setupJDA(SettingsManager settings) throws LoginException{
        System.out.println("Setting up bot...");
        JDABuilder builder = new JDABuilder(settings.getToken());
        builder.setDisabledCacheFlags(EnumSet.of(CacheFlag.EMOTE, CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE, CacheFlag.CLIENT_STATUS));
        builder.setGuildSubscriptionsEnabled(false);
        builder.setChunkingFilter(ChunkingFilter.NONE);
        builder.setActivity(Activity.listening("to old memes I missed"));
        builder.addEventListeners(new MessageListener(settings));
        return builder.build();
    }

    /**
     * Main method of the bot
     * @param args Unused args strings, can be used i.e. for token.
     */
    public static void main(String[] args) throws LoginException, InterruptedException {
        System.out.println("Initializing bot values...");
        SettingsManager settings = new SettingsManager();
        JDA jda = setupJDA(settings);
        jda.awaitReady();
        Scanner scanner = new Scanner(System.in);
        while(!scanner.nextLine().equals("stop")){}//Shutdown when the user inputs stop
        jda.shutdown();
        //Manually shutdown the OkHttpClient to make sure the client doesn't halt the shutdown
        OkHttpClient client = jda.getHttpClient();
        client.connectionPool().evictAll();
        client.dispatcher().executorService().shutdown();

    }
}
