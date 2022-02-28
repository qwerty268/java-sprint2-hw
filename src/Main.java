import tracker.exception.ManagerSaveException;
import tracker.model.Epic;
import tracker.model.Status;
import tracker.model.SubTask;
import tracker.model.Task;
import tracker.service.manager.inMemoryManager.FileBackedTasksManager;
import tracker.service.manager.inMemoryManager.InMemoryHistoryManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Scanner scanner = new Scanner(System.in);
        FileBackedTasksManager manager = new FileBackedTasksManager(historyManager);

        List<Task> history;

        System.out.println("Считать данные из файла - 0\n" +
                "Создать новые - 1");

        int choice = scanner.nextInt();

        if (choice == 0) {
            manager.read();
        } else {
            Task task1 = new Task(1, "task1", "task1", Status.DONE);
            Task task2 = new Task(2, "task2", "task2", Status.DONE);

            SubTask subTask3 = new SubTask(3, "subTask3", "subtask3", Status.DONE);
            SubTask subTask4 = new SubTask(4, "subTask4", "subTask4", Status.NEW);
            SubTask subTask5 = new SubTask(5, "subTask5", "subTask5", Status.NEW);

            Epic epic0 = new Epic(0, "epic0", "epic0", Status.NEW, new ArrayList<>(List.of(subTask3, subTask4, subTask5)));

            Epic epic6 = new Epic(6, "epic6", "epic6", Status.NEW, null);


            manager.addAnyTypeOfTask(task1);
            manager.addAnyTypeOfTask(task2);

            manager.getTask(task1.getTaskId());
            manager.getTask(task2.getTaskId());

            manager.addAnyTypeOfTask(subTask3);
            manager.addAnyTypeOfTask(subTask4);
            manager.addAnyTypeOfTask(subTask5);

            manager.addAnyTypeOfTask(epic0);
            manager.addAnyTypeOfTask(epic6);

            manager.getSubTask(3);
            manager.getSubTask(4);
            manager.getSubTask(5);
            manager.getEpic(0);
            manager.getEpic(6);
        }
        history = manager.historyManager.getHistory();

        System.out.println("Все таски: " + manager.getTasks().toString() + " " +manager.getEpics().toString() + " " +
                manager.getSubtasks().toString());

        System.out.println("История: " + history.toString());
    }
}
