package tracker.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private String url;
    private String API_KEY;

    public KVTaskClient(String url) {
        this.url = url;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "register"))
                .GET()
                .build();

        HttpResponse<String> response = null;

        try {
            HttpClient client = HttpClient.newHttpClient();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response != null) {
                API_KEY = response.body();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и повторите попытку.");
        }
    }


    public void put(String id, String json) {
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "save/" + id + "?API_KEY=" + API_KEY))
                    .POST(body)
                    .build();

            HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    public HttpTaskManager load(String id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "load/" + id + "?API_KEY=" + API_KEY))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            String body = response.body();

            return gson.fromJson(body, HttpTaskManager.class);
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
