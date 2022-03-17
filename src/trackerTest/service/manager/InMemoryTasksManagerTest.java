package trackerTest.service.manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.model.Status;
import tracker.model.Task;
import tracker.service.manager.inMemoryManager.InMemoryTasksManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

class InMemoryTasksManagerTest implements BaseTaskManagerTest {

    private InMemoryTasksManager tasksManager;

    @BeforeEach
    public void createTaskManager() {
        tasksManager = new InMemoryTasksManager();
    }

    @Test
    @Override
    public void testAddingAnyTypeOfTask() {
        Task task1 = new Task(1, "task1", "task1", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2017, 11, 11, 1, 1));

        tasksManager.addAnyTypeOfTask(task1);

        Assertions.assertEquals(tasksManager.getTask(1), task1);
    }

    @Test
    @Override
    public void testUpdatingTask() {
        Task task1 = new Task(1, "task1", "task1", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2017, 11, 11, 1, 1));

        tasksManager.addAnyTypeOfTask(task1);

        task1 = new Task(1, "task2", "task2", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2018, 11, 11, 1, 1));

        tasksManager.updateTask(task1);

        Assertions.assertEquals(tasksManager.getTask(1), task1);
    }

    @Override
    public void testDeletingAll() {

    }

    @Override
    public void testDeletingByIndex() {

    }
}