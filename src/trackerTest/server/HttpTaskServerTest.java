package trackerTest.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.Status;
import tracker.model.SubTask;
import tracker.model.Task;
import tracker.server.HttpTaskManager;
import tracker.server.HttpTaskServer;
import tracker.server.KVServer;
import tracker.service.manager.Managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class HttpTaskServerTest {
    private KVServer KVS;
    private HttpTaskManager taskManager;
    private HttpTaskServer server;

    private Task task1 = new Task(1, "task1", "task1Test", Status.NEW, Duration.ZERO, LocalDateTime.MAX);

    private SubTask subTask3 = new SubTask(3, "subTask3", "subtask3", Status.DONE, Duration.ofSeconds(10),
            LocalDateTime.of(2019, 11, 11, 1, 1));

    private SubTask subTask4 = new SubTask(4, "subTask4", "subTask4", Status.DONE, Duration.ofSeconds(10),
            LocalDateTime.of(2010, 11, 11, 1, 1));

    private Epic epic0 = new Epic(0, "epic0", "epic0", null,
            new ArrayList<>(List.of(subTask3, subTask4)));


    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    @BeforeEach
    public void beforeEach() throws IOException {
        KVS = new KVServer();
        KVS.start();
        taskManager = Managers.getDefault("http://localhost:8008/", 1);
        server = new HttpTaskServer(taskManager);


        taskManager.addAnyTypeOfTask(task1);
        taskManager.addAnyTypeOfTask(subTask3);
        taskManager.addAnyTypeOfTask(subTask4);
        taskManager.addAnyTypeOfTask(epic0);
    }

    @AfterEach
    public void afterEach() {
        KVS.stop();
        server.stop();
    }

    @Test
    public void gettingAndDeletingTaskByIdTest() {
        String body = "";
        try {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            body = response.body();
        } catch (IOException | InterruptedException ex) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и повторите попытку.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова.");
        }
        Assertions.assertEquals(gson.toJson(task1), body);

        try {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
            HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            body = response.body();
        } catch (IOException | InterruptedException ex) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и повторите попытку.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова.");
        }

        Assertions.assertEquals("Задача удалена", body);
    }

    @Test
    public void gettingDeletingPostingTasks() {
        String body = "";
        Task task2 = new Task(2, "task2", "task2Test", Status.NEW, Duration.ZERO,
                LocalDateTime.of(2000, 2, 2, 2, 2));

        taskManager.addAnyTypeOfTask(task2);

        try {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/task/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            body = response.body();
        } catch (IOException | InterruptedException ex) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и повторите попытку.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова.");
        }
        Assertions.assertEquals(gson.toJson(List.of(task1, task2)), body);


        try {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/task/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException ex) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и повторите попытку.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова.");
        }
        Assertions.assertEquals(List.of(), taskManager.getTasks());


        try {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/task/");

            String json = gson.toJson(task1);
            final HttpRequest.BodyPublisher bodyOfRequest = HttpRequest.BodyPublishers.ofString(json);

            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(bodyOfRequest).build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException ex) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и повторите попытку.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова.");
        }
        Assertions.assertEquals(List.of(task1), taskManager.getTasks());
    }


    @Test
    public void gettingSubsOfEpicTest() {
        String body = "";

        try {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/subtask/epic/?id=0");
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            body = response.body();
        } catch (IOException | InterruptedException ex) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и повторите попытку.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова.");
        }

        Assertions.assertEquals(gson.toJson(taskManager.getSubTasksOfEpic(0L)), body);
    }


    @Test
    public void gettingAndDeletingSubs() {
        String body = "";

        try {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/subtasks/?id=3");
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            body = response.body();
        } catch (IOException | InterruptedException ex) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и повторите попытку.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова.");
        }

        Assertions.assertEquals(gson.toJson(taskManager.getSubTask(3L)), body);


        try {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/subtask/epic/?id=3");
            HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            body = response.body();
        } catch (IOException | InterruptedException ex) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и повторите попытку.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова.");
        }

        Assertions.assertEquals(taskManager.getSubTasksOfEpic(3L), null);
    }

    @Test
    public void gettingDeletingPostingSubTasksTest() {
        String body = "";

        try {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/subtasks/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            body = response.body();
        } catch (IOException | InterruptedException ex) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и повторите попытку.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова.");
        }

        Assertions.assertEquals(gson.toJson(taskManager.getSubtasks()), body);


        try {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/subtasks/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            body = response.body();
        } catch (IOException | InterruptedException ex) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и повторите попытку.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова.");
        }

        Assertions.assertEquals(taskManager.getSubtasks(), List.of());


        try {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/subtasks/");

            String json = gson.toJson(subTask3);
            final HttpRequest.BodyPublisher bodyOfRequest = HttpRequest.BodyPublishers.ofString(json);

            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(bodyOfRequest).build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException ex) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и повторите попытку.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова.");
        }

        Assertions.assertEquals(gson.toJson(taskManager.getSubTask(3L)), gson.toJson(subTask3));
    }


    @Test
    public void gettingDeletingPostingEpicTest() {
        String body = "";

        try {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/epic/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            body = response.body();
        } catch (IOException | InterruptedException ex) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и повторите попытку.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова.");
        }

        Assertions.assertEquals(gson.toJson(taskManager.getEpics()), body);


        try {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/epic/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException ex) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и повторите попытку.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова.");
        }

        Assertions.assertEquals(taskManager.getSubtasks(), List.of());


        try {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/epic/");

            String json = gson.toJson(epic0);
            final HttpRequest.BodyPublisher bodyOfRequest = HttpRequest.BodyPublishers.ofString(json);

            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(bodyOfRequest).build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException ex) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и повторите попытку.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова.");
        }

        Assertions.assertEquals(gson.toJson(taskManager.getEpic(0L)), gson.toJson(epic0));
    }

    @Test
    public void gettingHistoryTest() {
        String body = "";

        taskManager.getTask(1);


        try {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks/history/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            body = response.body();
        } catch (IOException | InterruptedException ex) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и повторите попытку.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова.");
        }

        Assertions.assertEquals(gson.toJson(taskManager.getHistory()), body);
    }

    @Test
    public void gettingTasksTest() {
        String body = "";

        try {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks");
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            body = response.body();
        } catch (IOException | InterruptedException ex) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и повторите попытку.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова.");
        }

        List<Task> tasks = taskManager.getTasks();
        tasks.addAll(taskManager.getEpics());
        tasks.addAll(taskManager.getSubtasks());

        Assertions.assertEquals(gson.toJson(tasks), body);
    }
}