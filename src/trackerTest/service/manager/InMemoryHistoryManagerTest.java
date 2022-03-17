package trackerTest.service.manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.model.Status;
import tracker.model.SubTask;
import tracker.model.Task;
import tracker.service.manager.inMemoryManager.InMemoryHistoryManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

class InMemoryHistoryManagerTest {


    private InMemoryHistoryManager historyManager;

    @BeforeEach
    public void createInMemoryManager() {

        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void addWithEmptyHistoryManager() {
        Task task1 = new Task(1, "task1", "task1", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2017, 11, 11, 1, 1));

        historyManager.add(task1);
        historyManager.getHistory();

        Assertions.assertEquals(historyManager.getHistory(), List.of(task1));
    }

    @Test
    public void addWithNotEmptyHistoryManager() {
        Task task1 = new Task(1, "task1", "task1", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2017, 11, 11, 1, 1));
        historyManager.add(task1);

        historyManager.add(task1);


        Assertions.assertEquals(historyManager.getHistory(), List.of(task1));
    }

    @Test
    public void addAndDeletingWithNotEmptyHistoryManager() {
        Task task1 = new Task(1, "task1", "task1", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2017, 11, 11, 1, 1));

        Task task2 = new Task(2, "task2", "task2", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2018, 11, 11, 1, 1));


        SubTask subTask3 = new SubTask(3, "subTask3", "subtask3", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2019, 11, 11, 1, 1));

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(subTask3);

        historyManager.remove(task2.getTaskId());  //удаление из середины
        Assertions.assertEquals(historyManager.getHistory(), List.of(subTask3, task1));

        historyManager.add(task2);
        historyManager.remove(task2.getTaskId());  //удаление из начала
        Assertions.assertEquals(historyManager.getHistory(), List.of(subTask3, task1));

        historyManager.add(task2);
        historyManager.remove(task1.getTaskId());   //удаление из конца
        Assertions.assertEquals(historyManager.getHistory(), List.of(task2,subTask3));
    }

    //все методы HistoryManager неоднократно использованы в коде выше, поэтому не смысла писать тесты для каждого по отдельности
}