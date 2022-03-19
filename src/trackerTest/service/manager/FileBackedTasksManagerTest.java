package trackerTest.service.manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.Status;
import tracker.model.SubTask;
import tracker.model.Task;
import tracker.service.manager.inMemoryManager.FileBackedTasksManager;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class FileBackedTasksManagerTest {

    private FileBackedTasksManager manager;
    private Task task1;
    private Task task2;
    private SubTask subTask3;
    private SubTask subTask4;
    private SubTask subTask5;
    private Epic epic0;
    private Epic epic6;

    private HashMap<Long, Task> tasks;

    @BeforeEach
    public void beforeEach() {
        manager = new FileBackedTasksManager(new File("info"));

        tasks = new HashMap<>();

        task1 = new Task(1, "task1", "task1", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2017, 11, 11, 1, 1));

        task2 = new Task(2, "task2", "task2", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2018, 11, 11, 1, 1));


        subTask3 = new SubTask(3, "subTask3", "subtask3", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2019, 11, 11, 1, 1));

        subTask4 = new SubTask(4, "subTask4", "subTask4", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2010, 11, 11, 1, 1));

        subTask5 = new SubTask(5, "subTask5", "subTask5", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2015, 11, 11, 1, 1));

        epic0 = new Epic(0, "epic0", "epic0", null,
                new ArrayList<>(List.of(subTask3, subTask4, subTask5)));

        epic6 = new Epic(6, "epic6", "epic6", null, null);

        tasks.put(task1.getTaskId(), task1);
        tasks.put(task2.getTaskId(), task2);
        tasks.put(subTask3.getTaskId(), subTask3);
        tasks.put(subTask4.getTaskId(), subTask4);
        tasks.put(subTask5.getTaskId(), subTask5);
        tasks.put(epic0.getTaskId(), epic0);
        tasks.put(epic6.getTaskId(), epic6);

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


    //производится сразу проверка на чтение и на запись
    @Test
    public void savingInFileAndReadingFromFileTest() {
        manager.save();

        FileBackedTasksManager newManager = new FileBackedTasksManager(new File("info"));
        newManager.read();

        boolean test = true;

        List<Task> listOfReadTasksFromFile = newManager.getTasks();

        for (Task task: listOfReadTasksFromFile) {
            if (!tasks.containsKey(task.getTaskId())) {
                test = false;
            }
        }


        Assertions.assertTrue(test);
    }

    //все методы, вызываемы в FileBackedTasksManager, наследуются из InMemoryTasksManager
    //поэтому тестов для InMemoryTasksManager достаточно
}