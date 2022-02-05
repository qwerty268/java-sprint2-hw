import tracker.model.Epic;
import tracker.model.Status;
import tracker.model.SubTask;
import tracker.model.Task;
import tracker.service.manager.InMemoryManager.InMemoryHistoryManager;
import tracker.service.manager.InMemoryManager.InMemoryTasksManager;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        InMemoryTasksManager manager = new InMemoryTasksManager(historyManager);

        List<Task> story;
        /*
        Task task1 = new Task(1,"task1","task1", Status.DONE);
        Task task2 = new Task(2,"task2","task2",Status.DONE);

        //проверка на 2 задачи:
        manager.addAnyTypeOfTask(task1);
        manager.addAnyTypeOfTask(task2);

        manager.getTask(task1.getTaskId());
        manager.getTask(task2.getTaskId());

        story = manager.historyManager.getHistory();
        System.out.println(story.toString());

        manager.getTask(task1.getTaskId());

        story = manager.historyManager.getHistory();

        System.out.println(story.toString());

        manager.deleteAll();

        story = manager.historyManager.getHistory();

        System.out.println(story.toString());
        */


        SubTask subTask1 = new SubTask(1,"subTask1", "subtask1", Status.DONE);
        SubTask subTask2 = new SubTask(2, "subTask2", "subTask2", Status.NEW);
        SubTask subTask3 = new SubTask(3, "subTask3", "subTask3", Status.NEW);

        Epic epic0 = new Epic(0, "epic0", "epic0", Status.NEW, new ArrayList<>(List.of(subTask1, subTask2, subTask3)));

        manager.addAnyTypeOfTask(subTask1);
        manager.addAnyTypeOfTask(subTask2);
        manager.addAnyTypeOfTask(subTask3);

        manager.addAnyTypeOfTask(epic0);

        manager.getSubTask(1);
        manager.getSubTask(2);
        manager.getSubTask(3);
        manager.getEpic(0);

        story = historyManager.getHistory();
        System.out.println(story.toString());

        historyManager.remove(0);

        story = historyManager.getHistory();

        System.out.println(story.toString());
    }
}
