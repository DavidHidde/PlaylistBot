package playlistbot;

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
    private Long lastMessageID;

    /**
     * The ID of the YouTube playlist that is to be used
     */
    private String playlistID;

    /**
     * The file that should be read and written to
     */
    private final File file;

    /**
     * Create the manager, loading in the values from the settings file or creating one.
     */
    public SettingsManager(){
        this.file = new File("settings.txt");
        if(file.exists()){
            loadValues();
        } else {
            createValues();
        }
    }

    /**
     * Load the values from a settings file
     */
    private boolean loadValues(){
        boolean correct = false;
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            token = br.readLine().split(":")[1];
            channelID = Long.parseLong(br.readLine().split(":")[1]);
            lastMessageID = Long.parseLong(br.readLine().split(":")[1]);
            playlistID = br.readLine().split(":")[1];
            correct = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Something went wrong while reading the settings file");
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Settings file incomplete");
        }
        return correct;
    }

    /**
     * Ask the user and create the new values
     */
    private void createValues(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please insert a token:");
        token = scanner.nextLine();
        System.out.println("Please insert a channel ID:");
        channelID = Long.parseLong(scanner.nextLine());
        lastMessageID = null;
        System.out.println("Please insert a playlist ID:");
        playlistID = scanner.nextLine();
    }

    /**
     * Write the values to the settings file
     * @return True if the values have been succesfully written, else false
     */
    public boolean writeValues(){
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));){
            bw.write("Token:"+token+System.lineSeparator());
            bw.write("channelID:"+channelID+System.lineSeparator());
            bw.write("messageID:"+lastMessageID+System.lineSeparator());
            bw.write("playlistID:"+playlistID+System.lineSeparator());
            bw.flush();
            return true;
        } catch (IOException e) {
            System.out.println("Could not create settings file");
            return false;
        }
    }

    public String getPlaylistID() {
        return playlistID;
    }

    public String getToken() {
        return token;
    }

    public long getChannelID() {
        return channelID;
    }

    public Long getLastMessageID() {
        return lastMessageID;
    }

    public void setLastMessageID(long lastMessageID) {
        this.lastMessageID = lastMessageID;
    }
}
