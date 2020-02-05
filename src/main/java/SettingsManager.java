import java.io.*;
import java.util.Scanner;

/**
 * Manager for the settings file. Manages reading, creating and editing the file.
 */
public class SettingsManager {

    /**
     * The token of the bot
     */
    private String token;

    /**
     * The channel ID the bot should take messages from
     */
    private long channelID;

    /**
     * The ID of the last message seen by the bot
     */
    private long lastMessageID;

    /**
     * Indicates if no error has occurred and everything has been set correctly
     */
    private boolean correct;

    /**
     * Create the manager, loading in the values from the settings file or creating one.
     */
    public SettingsManager(){
        correct = false;
        File file = new File("settings.txt");
        if(file.exists()){
            loadValues(file);
        } else {
            createValues(file);
        }
    }

    /**
     * Load the values from a settings file
     * @param file The settings file that should be loaded in
     */
    private void loadValues(File file){
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            token = br.readLine().split(":")[1];
            channelID = Long.parseLong(br.readLine().split(":")[1]);
            lastMessageID = Long.parseLong(br.readLine().split(":")[1]);
            correct = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Something went wrong while reading the settings file");
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Settings file incomplete");
        }
    }

    /**
     * Create a new settings file, asking the user for the values
     */
    private void createValues(File file){
        Scanner scanner = new Scanner(System.in);
        String line;
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));){
            System.out.println("Please insert a token:");
            line = scanner.nextLine();
            bw.write("Token:"+line+System.lineSeparator());
            System.out.println("Please insert a channel ID:");
            line = scanner.nextLine();
            bw.write("channelID:"+line+System.lineSeparator());
            bw.write("messageID:"+System.lineSeparator());
            bw.flush();
            correct = true;
        } catch (IOException e) {
            System.out.println("Could not create settings file");
        }
    }

    public String getToken() {
        return token;
    }

    public long getChannelID() {
        return channelID;
    }

    public long getLastMessageID() {
        return lastMessageID;
    }

    public boolean isCorrect() {
        return correct;
    }
}
