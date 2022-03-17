package trackerTest.service.manager;

import org.junit.jupiter.api.Test;
import tracker.model.Task;
import tracker.service.manager.TaskManager;

public abstract interface BaseTaskManagerTest<T extends TaskManager> {
    //добавляет задачу любого типа
    @Test
    public void testAddingAnyTypeOfTask();

    //обновляет задачу любого вида
    @Test
    public void testUpdatingTask();

    //удаляет все задачи
    @Test
    public void testDeletingAll();

    //удаляет задачу любого типа по индексу
    @Test
    public void testDeletingByIndex();

    //возвращает истоирю последних 10 просмотренных задач
}
