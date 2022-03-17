package trackerTest.service.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.service.manager.inMemoryManager.InMemoryTasksManager;

class FileBackedTasksManagerTest implements BaseTaskManagerTest {

    private InMemoryTasksManager tasksManager;

    @BeforeEach
    public void createTaskManager() {
        tasksManager = new InMemoryTasksManager();
    }

    @Test
    @Override
    public void testAddingAnyTypeOfTask() {

    }

    @Override
    public void testUpdatingTask() {

    }

    @Override
    public void testDeletingAll() {

    }

    @Override
    public void testDeletingByIndex() {

    }
}