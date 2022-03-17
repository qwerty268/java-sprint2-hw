package tracker.service.manager.inMemoryManager;

import tracker.model.Epic;
import tracker.model.Status;
import tracker.model.SubTask;
import tracker.model.Task;
import tracker.service.manager.TaskManager;

import java.util.*;

public class InMemoryTasksManager implements TaskManager {
    public InMemoryHistoryManager historyManager = new InMemoryHistoryManager();


    final protected HashMap<Long, Task> tasks = new HashMap<>();
    final protected HashMap<Long, Epic> epics = new HashMap<>();
    final protected HashMap<Long, SubTask> subtasks = new HashMap<>();

    private final Comparator<Task> comparator = (Task task1, Task task2) -> {
        if (task1.getLocalDate().isBefore(task2.getLocalDate())) {
            return 1;
        } else return -1;
    };

    final protected TreeSet<Task> sortedTasks = new TreeSet<>(comparator);

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
            tasks.put(task.getTaskId(), task);
        }

        if (task.getClass() == Epic.class) {
            epics.put(task.getTaskId(), (Epic) task);
            ArrayList<SubTask> subT = ((Epic) task).getSubTasks();
            boolean isDone = true;
            int numOfDone = 0;
            for (SubTask i : subT) {
                if (i.getStatus() == Status.valueOf("NEW")) {
                    isDone = false;
                } else {
                    numOfDone += 1;
                }
            }
            if (isDone) {
                task.setStatus(Status.DONE);
            } else if (numOfDone == 0) {
                task.setStatus(Status.NEW);
            } else {
                task.setStatus(Status.IN_PROGRESS);
            }
        }

        if (task.getClass() == SubTask.class) {
            subtasks.put(task.getTaskId(), (SubTask) task);
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
                sortedTasks.remove(subTask.getTaskId());
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

    public void getPrioritizedTasks() {
        System.out.println(sortedTasks);
    }
}
