package tracker.service.manager;

import tracker.model.SubTask;
import tracker.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    //возвращает спсок задач
    ArrayList<Task> getTasks();

    //добавляет задачу любого типа
    void addAnyTypeOfTask(Task task);

    //обновляет задачу любого вида
    void updateTask(Task task);

    //удаляет все задачи
    void deleteAll();

    //удаляет задачу любого типа по индексу
    void deleteByIndex(long index);

    //возвращает задачи, отсортированные по времени
    void getPrioritizedTasks();

    //возвращает историю вызова задач
    List<Task> getHistory();

    //удаляет задачи
    void deleteTasks();

    //удаляет подзажачи
    void deleteSubTasks();

    //удаляет эпики
    void deleteEpics();

    //возвращет сабы эпика
    ArrayList<SubTask> getSubTasksOfEpic(Long epicId);

}
