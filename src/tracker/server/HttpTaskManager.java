package tracker.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tracker.model.Epic;
import tracker.model.SubTask;
import tracker.model.Task;
import tracker.service.manager.inMemoryManager.FileBackedTasksManager;
import tracker.service.manager.inMemoryManager.InMemoryTasksManager;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

    private String API_KEY;

    public HttpTaskManager(File file) {
        super(file);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8008/register"))
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
            e.printStackTrace();
        }
    }


    @Override
    public void addAnyTypeOfTask(Task task) {
        super.addAnyTypeOfTask(task);
        saveToServer(task);
    }


    public void saveToServer(Task task) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        if (task.getClass() == Task.class) {
            String json = gson.toJson(task);
            sendJsonObj(task.getTaskId(), json);
        }

        if (task.getClass() == SubTask.class) {
            String json = gson.toJson((SubTask) task);
            sendJsonObj(task.getTaskId(), json);
        }

        if (task.getClass() == Epic.class) {
            String json = gson.toJson((Epic) task);
            sendJsonObj(task.getTaskId(), json);
        }

    }

    public void getTaskFromServer(long id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8008/load/" + id + "?API_KEY=" + API_KEY))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            String body = response.body();

            if (body.contains("\"type\": \"EPIC\"")) {
                Epic epic = gson.fromJson(body, Epic.class);
                epics.put(epic.getTaskId(), epic);
                System.out.println("Епик загружен с сервера: " + epic.toString());
            } else if (body.contains("\"type\": \"SUBTASK\"")) {
                SubTask subTask = gson.fromJson(body, SubTask.class);
                subtasks.put(subTask.getTaskId(), subTask);
                System.out.println("Подзадача загружена с сервера: "+ subTask.toString());
            } else if (body.contains("\"type\": \"TASK\"")) {
                Task task = gson.fromJson(body, Task.class);
                tasks.put(task.getTaskId(), task);
                System.out.println("Задача загружена с сервера: " + task.toString());
            }

        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    private void sendJsonObj(long id, String json) {
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8008/save/" + id + "?API_KEY=" + API_KEY))
                    .GET()
                    .POST(body)
                    .build();

            HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }

}
