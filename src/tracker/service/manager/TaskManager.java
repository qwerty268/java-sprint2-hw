package tracker.service.manager;

import tracker.model.Task;

public interface TaskManager {


    //добавляет задачу любого типа
    public void addAnyTypeOfTask(Task task);

    //обновляет задачу любого вида
    public void updateTask(Task task);

    //удаляет все задачи
    public void deleteAll();

    //удаляет задачу любого типа по индексу
    public void deleteByIndex(long index);

    //возвращает истоирю последних 10 просмотренных задач
}
