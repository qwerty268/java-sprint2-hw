package Main.main;

import tracker.model.Epic;
import tracker.model.Status;
import tracker.model.SubTask;
import tracker.model.Task;
import tracker.server.HttpTaskManager;
import tracker.server.HttpTaskServer;
import tracker.server.KVServer;
import tracker.service.manager.Managers;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        KVServer KVS = new KVServer();
        KVS.start();
        HttpTaskManager taskManager = Managers.getDefault("http://localhost:8008/", 1);
        HttpTaskServer server = new HttpTaskServer(taskManager);


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

    }
}
