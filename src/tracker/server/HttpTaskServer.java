package tracker.server;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import tracker.model.SubTask;
import tracker.model.Task;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpTaskServer {
    private HttpServer server;
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private HttpTaskManager taskManager = new HttpTaskManager(new File("info"));

    public HttpTaskServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);

        server.createContext("/tasks", this::handle);

    }

    private void handle(HttpExchange h) throws IOException {

        String pathOfRequest = h.getRequestURI().getPath();
        if (pathOfRequest.contains("tasks/task/&d=")) {
            try {

                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .create();

                String path = h.getRequestURI().getPath();
                String id = path.substring(path.indexOf('='));

                switch (h.getRequestMethod()) {
                    case "GET":

                        Task task = getTaskById(Integer.parseInt(id));

                        String response = gson.toJson(task);

                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                    case "DELETE":
                        taskManager.deleteByIndex(Integer.parseInt(id));

                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("Задача удалена".getBytes());
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
        } else if (pathOfRequest.contains("tasks/task/")) {
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
                            os.write(response.getBytes());
                        }
                        break;
                    case "DELETE":
                        taskManager.deleteTasks();

                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("Все задачи удалены".getBytes());
                        }
                        break;
                    case "POST":
                        String taskInString = new String(h.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                        Task task = gson.fromJson(taskInString, Task.class);

                        taskManager.addAnyTypeOfTask(task);

                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("Задача добавлена".getBytes());
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
        } else if (pathOfRequest.contains("tasks/subtask/epic/?d=")) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
            switch (h.getRequestMethod()) {
                case "GET":
                    String path = h.getRequestURI().getPath();
                    String id = path.substring(path.indexOf('='));

                    List<SubTask> subTasks = taskManager.getSubTasksOfEpic(Long.parseLong(id));
                    String response = gson.toJson(subTasks);

                    h.sendResponseHeaders(200, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    break;
                default:
                    System.out.println("tasks/subtask/epic/?d= ждёт GET-запрос, а получил " + h.getRequestMethod());
                    h.sendResponseHeaders(405, 0);
            }

        } else if (pathOfRequest.contains("tasks/subtasks/?d=")) {
            try {

                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .create();

                String path = h.getRequestURI().getPath();
                String id = path.substring(path.indexOf('='));

                switch (h.getRequestMethod()) {
                    case "GET":

                        SubTask subTask = (SubTask) getTaskById(Integer.parseInt(id));

                        String response = gson.toJson(subTask);

                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                    case "DELETE":
                        taskManager.deleteByIndex(Integer.parseInt(id));

                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("Задача удалена".getBytes());
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
        } else if (pathOfRequest.contains("tasks/subtasks/")) {
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
                            os.write(response.getBytes());
                        }
                        break;
                    case "DELETE":
                        taskManager.deleteSubTasks();

                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("Все подзадачи удалены".getBytes());
                        }
                        break;
                    case "POST":
                        String taskInString = new String(h.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                        Task task = gson.fromJson(taskInString, SubTask.class);

                        taskManager.addAnyTypeOfTask(task);

                        h.sendResponseHeaders(200, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("Подзадача добавлена".getBytes());
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
        } else if ()
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
