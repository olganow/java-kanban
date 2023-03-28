package main.java.http;

import com.google.gson.Gson;
import main.java.description.TaskStatus;
import main.java.manager.ManagerSaveException;
import main.java.manager.Managers;
import main.java.task.SingleTask;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;


public class KVTaskClient {
    private final String apiToken;
    private final String clientServerUrl;
    private HttpClient client = HttpClient.newHttpClient();

    public KVTaskClient(String clientServerUrl) {
        this.clientServerUrl = clientServerUrl;
        apiToken = getToken();
    }

    private String getToken() {
        HttpRequest request = HttpRequest.newBuilder() // создаём объект, описывающий HTTP-запрос
                .uri(URI.create(clientServerUrl + "/register")) // указываем адрес ресурса
                .GET() // указываем HTTP-метод запроса
                .build();
        System.out.println("\n/register");
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("API_TOKEN HttpClient: = " + response.body());
                return response.body();
            } else {
                throw new ManagerSaveException("/register is wanting for GET-request, but statusCode is " + response.statusCode());
            }
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void put(String key, String json) {
        try {
            URI uri = URI.create(clientServerUrl + "/save/" + key + "?API_TOKEN=" + apiToken);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("response.statusCode() != 200");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String load(String key) {
        try {
            URI uri = URI.create(clientServerUrl + "/load/" + key + "?API_TOKEN=" + apiToken);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("response.statusCode() != 200");
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        KVServer kvServer = Managers.getDefaultKVServer();
        kvServer.start();
        KVTaskClient kvTaskClient = new KVTaskClient("http://localhost:8078");
        Gson gson = new Gson();

        SingleTask singleTask0 = new SingleTask(0, "Single safe Task", "Desc SST", TaskStatus.NEW);
        SingleTask singleTask2 = new SingleTask(1, "Another safe Task", "Desc AST", TaskStatus.NEW,
                Instant.ofEpochMilli(1704056400000L), 707568400L);


        kvTaskClient.put("1", gson.toJson(singleTask0));
        kvTaskClient.put("2", gson.toJson(singleTask2));

        System.out.println(gson.fromJson(kvTaskClient.load("1"), SingleTask.class));
        System.out.println(gson.fromJson(kvTaskClient.load("2"), SingleTask.class));

        kvServer.stop();
    }
}
