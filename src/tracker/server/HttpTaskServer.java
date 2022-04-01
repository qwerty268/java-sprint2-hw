package tracker.server;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import tracker.model.Epic;
import tracker.model.Status;
import tracker.model.SubTask;
import tracker.model.Task;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskServer {
    private HttpServer server;
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private static HttpTaskManager taskManager;

    public static void main(String[] args) throws IOException {
        taskManager = new HttpTaskManager(new File("info"));
        Task task1 = new Task(1, "task1", "task1Test", Status.NEW, Duration.ZERO, LocalDateTime.MAX);

        SubTask subTask3 = new SubTask(3, "subTask3", "subtask3", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2019, 11, 11, 1, 1));

        SubTask subTask4 = new SubTask(4, "subTask4", "subTask4", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2010, 11, 11, 1, 1));

        Epic epic0 = new Epic(0, "epic0", "epic0", null,
                new ArrayList<>(List.of(subTask3, subTask4)));

        taskManager.addAnyTypeOfTask(task1);
        taskManager.addAnyTypeOfTask(subTask3);
        taskManager.addAnyTypeOfTask(subTask4);
        taskManager.addAnyTypeOfTask(epic0);

        HttpTaskServer httpTaskServer = new HttpTaskServer();
    }

    public HttpTaskServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);

        server.createContext("/tasks", this::handle);

        server.start();
    }

    private void handle(HttpExchange h) throws IOException {
        System.out.println(h.getRequestURI());
        String pathOfRequest = h.getRequestURI().toString();

        if (pathOfRequest.contains("/tasks/task/?d=")) { //++
            try {

                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .create();

                String id = pathOfRequest.substring(pathOfRequest.indexOf('=') + 1);

                switch (h.getRequestMethod()) {
                    case "GET":

                        Task task = taskManager.getTask(Integer.parseInt(id));

                        String response = gson.toJson(task);

                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                        break;
                    case "DELETE":
                        taskManager.deleteByIndex(Integer.parseInt(id));

                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("Задача удалена".getBytes(DEFAULT_CHARSET));
                        }
                        break;
                    default:
                        System.out.println("tasks/task/&d= ждёт GET/DELETE-запрос, а получил " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                }
            } finally {
                h.close();
                return;
            }
        } else if (pathOfRequest.contains("tasks/task/")) { //++
            try {
                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .serializeNulls()
                        .create();

                switch (h.getRequestMethod()) {
                    case "GET":
                        String response = gson.toJson(taskManager.getTasks());

                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                        break;
                    case "DELETE":
                        taskManager.deleteTasks();

                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("Все задачи удалены".getBytes(DEFAULT_CHARSET));
                        }
                        break;
                    case "POST":
                        String taskInString = new String(h.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                        Task task = gson.fromJson(taskInString, Task.class);

                        taskManager.addAnyTypeOfTask(task);

                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("Задача добавлена".getBytes(DEFAULT_CHARSET));
                        }
                        break;
                    default:
                        System.out.println("/tasks/task/ ждёт GET-запрос, а получил " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                }
            } finally {
                h.close();
                return;
            }
        } else if (pathOfRequest.contains("tasks/subtask/epic/?d=")) { //++
            try {
                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .create();
                switch (h.getRequestMethod()) {
                    case "GET": ;
                        String id = pathOfRequest.substring(pathOfRequest.indexOf('=') + 1);

                        List<SubTask> subTasks = taskManager.getSubTasksOfEpic(Long.parseLong(id));
                        String response = gson.toJson(subTasks);

                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                        break;
                    default:
                        System.out.println("tasks/subtask/epic/?d= ждёт GET-запрос, а получил " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                }
            } finally {
                h.close();
                return;
            }
        } else if (pathOfRequest.contains("tasks/subtasks/?d=")) { //++
            try {

                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .create();

                String id = pathOfRequest.substring(pathOfRequest.indexOf('=') + 1);

                switch (h.getRequestMethod()) {
                    case "GET":

                        SubTask subTask = (SubTask) getTaskById(Integer.parseInt(id));

                        String response = gson.toJson(subTask);

                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                        break;
                    case "DELETE":
                        taskManager.deleteByIndex(Integer.parseInt(id));

                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("Подзадача удалена".getBytes(DEFAULT_CHARSET));
                        }
                        break;
                    default:
                        System.out.println("tasks/subtasks/?d= ждёт GET/DELETE-запрос, а получил " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                }
            } finally {
                h.close();
                return;
            }
        } else if (pathOfRequest.contains("tasks/subtasks/")) { //
            try {
                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .serializeNulls()
                        .create();

                switch (h.getRequestMethod()) {
                    case "GET":
                        String response = gson.toJson(taskManager.getSubtasks());

                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                        break;
                    case "DELETE":
                        taskManager.deleteSubTasks();

                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("Все подзадачи удалены".getBytes(DEFAULT_CHARSET));
                        }
                        break;
                    case "POST":
                        String taskInString = new String(h.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                        Task task = gson.fromJson(taskInString, SubTask.class);

                        taskManager.addAnyTypeOfTask(task);

                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("Подзадача добавлена".getBytes(DEFAULT_CHARSET));
                        }
                        break;
                    default:
                        System.out.println("tasks/subtasks/ ждёт GET/DELETE/POST-запрос, а получил " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                }
            } finally {
                h.close();
                return;
            }
        } else if (pathOfRequest.contains("tasks/epic/")) { //
            try {
                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .serializeNulls()
                        .create();

                switch (h.getRequestMethod()) {
                    case "GET":
                        String response = gson.toJson(taskManager.getEpics());

                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                        break;
                    case "DELETE":
                        taskManager.deleteEpics();

                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("Все епики удалены".getBytes(DEFAULT_CHARSET));
                        }
                        break;
                    case "POST":
                        String taskInString = new String(h.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                        Epic epic = gson.fromJson(taskInString, Epic.class);

                        taskManager.addAnyTypeOfTask(epic);

                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("Эпик добавлен".getBytes(DEFAULT_CHARSET));
                        }
                        break;
                    default:
                        System.out.println("tasks/epic/ ждёт GET/DELETE/POST-запрос, а получил " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                }
            } finally {
                h.close();
                return;
            }
        } else if (pathOfRequest.contains("tasks/history")) { //+
            try {
                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .serializeNulls()
                        .create();
                switch (h.getRequestMethod()) {
                    case "GET":
                        List<Task> history = taskManager.getHistory();
                        String response = gson.toJson(history);

                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
                        break;
                    default:
                        System.out.println("tasks/history ждёт GET-запрос, а получил " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                }
            } finally {
                h.close();
                return;
            }
        } else {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .serializeNulls()
                    .create();

            List<Task> tasks = taskManager.getTasks();
            tasks.addAll(taskManager.getEpics());
            tasks.addAll(taskManager.getSubtasks());

            String response = gson.toJson(tasks);

            h.sendResponseHeaders(200, 0);
            try (OutputStream os = h.getResponseBody()) {
                os.write(response.getBytes(DEFAULT_CHARSET));
            }
        }
    }

    private Task getTaskById(long id) {
        Task task = taskManager.getSubTask(id);
        if (task == null) {
            task = taskManager.getEpic(id);
        }

        if (task == null) {
            task = taskManager.getTask(id);
        }
        return task;
    }
}
