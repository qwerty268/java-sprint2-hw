package trackerTest.service.manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.Status;
import tracker.model.SubTask;
import tracker.model.Task;
import tracker.service.manager.inMemoryManager.InMemoryTasksManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTasksManagerTest {

    private static Task task1;
    private static SubTask subTask3;
    private  static SubTask subTask4;
    private static Epic epic0;
    private static InMemoryTasksManager manager;

    @BeforeAll
    static void beforeAll() {
        manager = new InMemoryTasksManager();
        task1 = new Task(1, "task1", "task1Test", Status.NEW, Duration.ZERO, LocalDateTime.MAX);

        subTask3 = new SubTask(3, "subTask3", "subtask3", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2019, 11, 11, 1, 1));

        subTask4 = new SubTask(4, "subTask4", "subTask4", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2010, 11, 11, 1, 1));

        epic0 = new Epic(0, "epic0", "epic0", null,
                new ArrayList<>(List.of(subTask3, subTask4)));
    }

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTasksManager();
    }


    @Test
    public void allCollectionsShouldBeEmpty(){
        manager.addAnyTypeOfTask(null);
        assertEquals(0, manager.getTasks().size());
    }


    @Test
    public void addingTaskTest(){
        manager.addAnyTypeOfTask(task1);
        assertEquals(manager.getTasks().size(), 1);
    }

    @Test
    public void addingSubTasksAndEpicTest() {
        manager.addAnyTypeOfTask(subTask3);
        manager.addAnyTypeOfTask(subTask4);
        manager.addAnyTypeOfTask(epic0);

        assertEquals(manager.getSubtasks().size(), 2);
        assertEquals(manager.getEpics().size(), 1);
        assertEquals(manager.getSubTasksOfEpic(0L).size(), 2);
    }

    @Test
    public void addingEpicTest() {
        manager.addAnyTypeOfTask(epic0);
        assertEquals(manager.getSubtasks().size(), 0);
        assertEquals(manager.getEpics().size(), 1);
    }

    @Test
    public void updatingTaskTest() {
        manager.addAnyTypeOfTask(task1);
        Task task = new  Task(1, "!!!!", "!!!!!", Status.NEW, Duration.ZERO, LocalDateTime.MAX);
        manager.updateTask(task);
        assertEquals(manager.getTask(1).getName(), "!!!!");
    }

    //методы связанные с историей проверены в InMemoryHistoryManagerTest
}