package trackerTest.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.Status;
import tracker.model.SubTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class EpicTest {
    @Test
    public void createEpicWithNewSubTasks() {
        SubTask subTask3 = new SubTask(3, "subTask3", "subtask3", Status.NEW, Duration.ofSeconds(10),
                LocalDateTime.of(2019, 11, 11, 1, 1));

        SubTask subTask4 = new SubTask(4, "subTask4", "subTask4", Status.NEW, Duration.ofSeconds(10),
                LocalDateTime.of(2010, 11, 11, 1, 1));

        SubTask subTask5 = new SubTask(5, "subTask5", "subTask5", Status.NEW, Duration.ofSeconds(10),
                LocalDateTime.of(2015, 11, 11, 1, 1));


        Epic epic0 = new Epic(0, "epic0", "epic0", null,
                new ArrayList<>(List.of(subTask3, subTask4, subTask5)));

        Assertions.assertEquals(epic0.getStatus(), Status.NEW);
    }

    @Test
    public void createEpicWithDoneSubTasks() {
        SubTask subTask3 = new SubTask(3, "subTask3", "subtask3", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2019, 11, 11, 1, 1));

        SubTask subTask4 = new SubTask(4, "subTask4", "subTask4", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2010, 11, 11, 1, 1));

        SubTask subTask5 = new SubTask(5, "subTask5", "subTask5", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2015, 11, 11, 1, 1));


        Epic epic0 = new Epic(0, "epic0", "epic0", null,
                new ArrayList<>(List.of(subTask3, subTask4, subTask5)));

        Assertions.assertEquals(epic0.getStatus(), Status.DONE);
    }

    @Test
    public void createEpicWithNewAndDoneSubTasks() {
        SubTask subTask3 = new SubTask(3, "subTask3", "subtask3", Status.NEW, Duration.ofSeconds(10),
                LocalDateTime.of(2019, 11, 11, 1, 1));

        SubTask subTask4 = new SubTask(4, "subTask4", "subTask4", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2010, 11, 11, 1, 1));

        SubTask subTask5 = new SubTask(5, "subTask5", "subTask5", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2015, 11, 11, 1, 1));


        Epic epic0 = new Epic(0, "epic0", "epic0", null,
                new ArrayList<>(List.of(subTask3, subTask4, subTask5)));

        Assertions.assertEquals(epic0.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    public void createEpicWithInProgressSubTasks() {
        SubTask subTask3 = new SubTask(3, "subTask3", "subtask3", Status.IN_PROGRESS, Duration.ofSeconds(10),
                LocalDateTime.of(2019, 11, 11, 1, 1));

        SubTask subTask4 = new SubTask(4, "subTask4", "subTask4", Status.IN_PROGRESS, Duration.ofSeconds(10),
                LocalDateTime.of(2010, 11, 11, 1, 1));

        SubTask subTask5 = new SubTask(5, "subTask5", "subTask5", Status.IN_PROGRESS, Duration.ofSeconds(10),
                LocalDateTime.of(2015, 11, 11, 1, 1));


        Epic epic0 = new Epic(0, "epic0", "epic0", null,
                new ArrayList<>(List.of(subTask3, subTask4, subTask5)));

        Assertions.assertEquals(epic0.getStatus(), Status.IN_PROGRESS);
    }
}