package playlistbot.youtube;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemSnippet;
import com.google.api.services.youtube.model.ResourceId;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.Collections;

/**
 * The Youtube requester handles the calls to the YouTube API and authentication
 */
class YoutubeRequester {

    private final String CLIENT_SECRETS= "client_secret.json";
    private final Collection<String> SCOPES = Collections.singletonList("https://www.googleapis.com/auth/youtube");
    private final String APPLICATION_NAME = "PlaylistBot";
    private JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private FileDataStoreFactory dataStoreFactory;

    /**
     * Get a new YoutubeRequester which can make requests to YouTube
     */
    YoutubeRequester(){
       File dir = new File("tokens");
       if(!dir.isDirectory()){
           dir.mkdir();
       }
        try {
            dataStoreFactory = new FileDataStoreFactory(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    private Credential authorize(final NetHttpTransport httpTransport) throws IOException {
        // Load client secrets.
        InputStream in = new FileInputStream(CLIENT_SECRETS);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(dataStoreFactory).build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    /**
     * Build and return an authorized API client service.
     *
     * @return an authorized API client service
     * @throws GeneralSecurityException, IOException
     */
    private YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize(httpTransport);
        return new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public void addPlaylistItem(String playListItem, String playListID){
        //Check if we haven't connected to youtube yet or the token has expired
        YouTube youtube = null;
        try {
            youtube = getService();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Couldn't connect to YouTube");
        }
        // Define the PlaylistItem object, which will be uploaded as the request body.
        PlaylistItem playlistItem = new PlaylistItem();

        // Add the snippet object property to the PlaylistItem object.
        PlaylistItemSnippet snippet = new PlaylistItemSnippet();
        snippet.setPlaylistId(playListID);
        snippet.setPosition(0L);
        ResourceId resourceId = new ResourceId();
        resourceId.setKind("youtube#video");
        resourceId.setVideoId(playListItem);
        snippet.setResourceId(resourceId);
        playlistItem.setSnippet(snippet);

        // Define and execute the API request
        try {
            YouTube.PlaylistItems.Insert request = youtube.playlistItems()
                    .insert("snippet", playlistItem);
            PlaylistItem response = request.execute();
            System.out.println(response);
        } catch (IOException e) {
            System.out.println("Could not insert video into playlist");
        }
    }
}
