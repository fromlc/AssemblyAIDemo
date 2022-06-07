package com.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import com.google.gson.Gson;

/**
 * Demo AssemblyAI API
 */
public final class AssemblyAIDemo {
    private AssemblyAIDemo() {}

    /**
     * Gets transcript for .mp4 file. URL needs argument: ?raw=true .mp4 file on GitHub must be in a
     * public repository. Writes transcript text to file transcript.txt.
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) throws Exception {

        Transcript transcript = new Transcript();
        transcript.setAudio_url(Constants.AUDIO_URL);
        // gson for working with JSON
        Gson gson = new Gson();
        String jsonRequest = gson.toJson(transcript);
        // check for correctly formed JSON request
        System.out.println(jsonRequest);

        // build POST request for transcript

        HttpRequest postRequest = HttpRequest.newBuilder().uri(new URI(Constants.ASSEMBLYAI_URL))
                .header("Authorization", Constants.API_KEY)
                .POST(BodyPublishers.ofString(jsonRequest)).build();

        HttpClient httpClient = HttpClient.newHttpClient();
        // send the request expecting String response
        HttpResponse<String> postResponse = httpClient.send(postRequest, BodyHandlers.ofString());
        // display respose body
        System.out.println(postResponse.body());

        // build GET request for transcript data

        // set transcript object fields from JSON response body
        transcript = gson.fromJson(postResponse.body(), Transcript.class);

        // GET is the default, so .GET() not needed
        // GET request needs forward slash, POST request did not
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI(Constants.ASSEMBLYAI_URL + "/" + transcript.getId()))
                .header("Authorization", Constants.API_KEY).build();

        String textData;

        while (true) {
            // send the request expecting String response
            HttpResponse<String> getResponse = httpClient.send(getRequest, BodyHandlers.ofString());

            // DEBUG check
            // String checkBody = getResponse.body();

            // set transcript object fields from JSON response body
            transcript = gson.fromJson(getResponse.body(), Transcript.class);

            String status = transcript.getStatus();
            System.out.println(status);

            if ("completed".equals(status)) {
                textData = transcript.getText();
                TextFileWriter tf = new TextFileWriter(Constants.TRANSCRIPT_FILE, textData);
                System.out.printf("Transcript text written to file %s\n\n", textData);
                break;
            }
            if ("error".equals(status)) {
                System.out.printf("Error: %s\n", transcript.getError());
                break;
            }
            Thread.sleep(1000);
        }

    }
}
