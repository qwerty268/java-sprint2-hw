package tracker.service;

import tracker.model.SubTask;
import tracker.model.Task;

import java.util.ArrayList;

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
    public ArrayList<Task> history();
}
