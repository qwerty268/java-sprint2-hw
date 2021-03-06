package tracker.service.manager.inMemoryManager;

import tracker.model.Epic;
import tracker.model.SubTask;
import tracker.model.Task;
import tracker.service.manager.TaskManager;

import java.util.*;

public class InMemoryTasksManager implements TaskManager {
    protected InMemoryHistoryManager historyManager = new InMemoryHistoryManager();


    protected HashMap<Long, Task> tasks = new HashMap<>();
    protected HashMap<Long, Epic> epics = new HashMap<>();
    protected HashMap<Long, SubTask> subtasks = new HashMap<>();

    private final Comparator<Task> comparator = (Task task1, Task task2) -> {
        if (task1.getLocalDate().isBefore(task2.getLocalDate())) {
            return 1;
        } else if (task1.getTaskId() == task2.getTaskId()) {
            return 0;
        } else return -1;
    };

    protected TreeSet<Task> sortedTasks = new TreeSet<>(comparator);

    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> arrayOfTasks = new ArrayList<>();

        if (!tasks.isEmpty()) {
            arrayOfTasks.addAll(tasks.values());
            return arrayOfTasks;
        }

        System.out.println("Задач нет");

        return arrayOfTasks;
    }

    public ArrayList<Task> getEpics() {
        ArrayList<Task> arrayOfEpics = new ArrayList<>();

        if (!epics.isEmpty()) {
            arrayOfEpics.addAll(epics.values());
            return arrayOfEpics;
        }

        System.out.println("Нет епиков");
        return arrayOfEpics;
    }

    public ArrayList<Task> getSubtasks() {
        ArrayList<Task> arrayOfEpics = new ArrayList<>();

        if (!subtasks.isEmpty()) {
            arrayOfEpics.addAll(subtasks.values());
            return arrayOfEpics;
        }

        System.out.println("Нет subtasks");
        return arrayOfEpics;
    }

    @Override
    public ArrayList<SubTask> getSubTasksOfEpic(Long epicId) {
        if (epics.get(epicId) == null) {
            return null;
        }

        return epics.get(epicId).getSubTasks();
    }

    @Override
    public void addAnyTypeOfTask(Task task) {
        if (task == null) {
            return;
        }
        if (!tasks.containsKey(task.getTaskId()) &&
                !tasks.containsKey(task.getTaskId()) &&
                !tasks.containsKey(task.getTaskId())) {
            doTheyOverlapInTime(task);
        }
        if (task.getClass() == Task.class) {
            tasks.put(task.getTaskId(), task);
        }

        if (task.getClass() == Epic.class) {
            epics.put(task.getTaskId(), (Epic) task);
        }

        if (task.getClass() == SubTask.class) {
            subtasks.put(task.getTaskId(), (SubTask) task);
        }

        sortedTasks.add(task);
    }

    @Override
    public void updateTask(Task task) {


        if (task.getClass() == Task.class) {
            sortedTasks.remove(tasks.get(task.getTaskId()));

            tasks.put(task.getTaskId(), task);
        }

        if (task.getClass() == Epic.class) {
            sortedTasks.remove(epics.get(task.getTaskId()));
            epics.put(task.getTaskId(), (Epic) task);
            ((Epic) task).calculateStatus();
        }

        if (task.getClass() == SubTask.class) {
            sortedTasks.remove(subtasks.get(task.getTaskId()));
            subtasks.put(task.getTaskId(), (SubTask) task);
            epics.values().forEach(task1 -> {
                if (task1.getSubTasks().contains(task)) {
                    task1.calculateStatus();
                    return;
                }
            });
        }

    }

    @Override
    public void deleteAll() {
        for (Long id : tasks.keySet()) historyManager.remove(id);
        tasks.clear();

        for (Long id : epics.keySet()) historyManager.remove(id);
        epics.clear();

        for (Long id : subtasks.keySet()) historyManager.remove(id);
        subtasks.clear();

        sortedTasks.clear();
    }

    @Override
    public void deleteByIndex(long index) {
        if (tasks.get(index) != null) {

            sortedTasks.remove(tasks.get(index));
            tasks.remove(index);

            historyManager.remove(index);
        }

        if (epics.get(index) != null) {
            historyManager.remove(index);

            for (Task subTask : epics.get(index).getSubTasks()) {
                subtasks.remove(subTask.getTaskId());
                sortedTasks.remove(subTask);
            }

            epics.get(index).clearSubTasks();
            sortedTasks.remove(epics.get(index));
            epics.remove(index);
        }

        if (subtasks.get(index) != null) {
            sortedTasks.remove(subtasks.get(index));
            subtasks.remove(index);

            historyManager.remove(index);
        }

    }

    public SubTask getSubTask(long subTaskId) {
        historyManager.add(subtasks.get(subTaskId));
        return subtasks.get(subTaskId);
    }

    public Epic getEpic(long epicId) {
        historyManager.add(epics.get(epicId));
        return epics.get(epicId);
    }

    public Task getTask(long taskId) {
        historyManager.add(tasks.get(taskId));
        return tasks.get(taskId);

    }

    @Override
    public void deleteTasks() {
        HashMap<Long, Task> tasksClone = (HashMap<Long, Task>) tasks.clone();
        for (Task task: tasksClone.values()) {
            deleteByIndex(task.getTaskId());
        }
    }

    @Override
    public void deleteSubTasks() {
        HashMap<Long, SubTask> subsClone = (HashMap<Long, SubTask>) subtasks.clone();
            for (SubTask subTask : subsClone.values()) {
                    deleteByIndex(subTask.getTaskId());
            }
    }

    @Override
    public void deleteEpics() {

        HashMap<Long, Epic> epicsClone = (HashMap<Long, Epic>) epics.clone();
        for (Epic epic: epicsClone.values()) {
            deleteByIndex(epic.getTaskId());
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void getPrioritizedTasks() {
        System.out.println(sortedTasks);
    }

    //проверяет на пересечение по времени исполнения
    private void doTheyOverlapInTime(Task task) throws IllegalArgumentException {
        if (task.getClass() == Epic.class) {
            return;
        }

        sortedTasks.forEach(task1 -> {
                    if (task.getLocalDate().plus(task.getDuration()).isAfter(task1.getLocalDate()) &&
                            task.getLocalDate().isBefore(task1.getLocalDate()) ||
                            task.getLocalDate().isEqual(task1.getLocalDate())) {
                        throw new IllegalArgumentException("Таски пересекаются по времени выполнения"); //выбрасывает ошибку, если таски пересекаются во времени
                    }
                }
        );
    }
}
