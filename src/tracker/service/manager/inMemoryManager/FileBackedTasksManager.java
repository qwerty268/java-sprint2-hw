package tracker.service.manager.inMemoryManager;

import tracker.exception.ManagerSaveException;
import tracker.model.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTasksManager {

    public static void main(String[] args) {
        FileBackedTasksManager manager = new FileBackedTasksManager(new File("info"));


        Task task1 = new Task(1, "task1", "task1", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2017, 11, 11, 1, 1));

        Task task2 = new Task(2, "task2", "task2", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2018, 11, 11, 1, 1));


        SubTask subTask3 = new SubTask(3, "subTask3", "subtask3", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2019, 11, 11, 1, 1));

        SubTask subTask4 = new SubTask(4, "subTask4", "subTask4", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2010, 11, 11, 1, 1));

        SubTask subTask5 = new SubTask(5, "subTask5", "subTask5", Status.DONE, Duration.ofSeconds(10),
                LocalDateTime.of(2015, 11, 11, 1, 1));

        Epic epic0 = new Epic(0, "epic0", "epic0", null,
                new ArrayList<>(List.of(subTask3, subTask4, subTask5)));

        Epic epic6 = new Epic(6, "epic6", "epic6", null, null);


        manager.addAnyTypeOfTask(task1);
        manager.addAnyTypeOfTask(task2);

        manager.getTask(task1.getTaskId());
        manager.getTask(task2.getTaskId());

        manager.addAnyTypeOfTask(subTask3);
        manager.addAnyTypeOfTask(subTask4);
        manager.addAnyTypeOfTask(subTask5);

        manager.addAnyTypeOfTask(epic0);
        manager.addAnyTypeOfTask(epic6);

        manager.getSubTask(3);
        manager.getSubTask(4);
        manager.getSubTask(5);
        manager.getEpic(0);
        manager.getEpic(6);


        System.out.println("Все таски: " + manager.getTasks().toString() + " " + manager.getEpics().toString() + " " +
                manager.getSubtasks().toString());

        System.out.println("История: " + manager.historyManager.getHistory().toString());
        System.out.println();

        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(new File("info"));

        System.out.println("Все таски: " + newManager.getTasks().toString() + " " + newManager.getEpics().toString() +
                " " + newManager.getSubtasks().toString());

        System.out.println("История: " + newManager.historyManager.getHistory().toString());

        newManager.getPrioritizedTasks();
    }


    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    @Override
    public void addAnyTypeOfTask(Task task) {
        super.addAnyTypeOfTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteAll() {
        super.deleteAll();
        save();
    }

    @Override
    public void deleteByIndex(long index) {
        super.deleteByIndex(index);
        save();
    }

    @Override
    public Task getTask(long taskId) {
        historyManager.add(tasks.get(taskId));
        save();
        return tasks.get(taskId);
    }

    @Override
    public SubTask getSubTask(long subTaskId) {
        historyManager.add(subtasks.get(subTaskId));
        save();
        return subtasks.get(subTaskId);
    }

    @Override
    public Epic getEpic(long epicId) {
        historyManager.add(epics.get(epicId));
        save();
        return epics.get(epicId);
    }

    public static FileBackedTasksManager loadFromFile(File newFile) {
        FileBackedTasksManager manager = new FileBackedTasksManager(newFile);

        manager.read();

        return manager;
    }

    public void save() {
        try (Writer fileWriter = new FileWriter(file, false)) {


            fileWriter.write("id,type,name,status,description,epic,duration,LocalDate\n");

            if (!tasks.isEmpty()) {
                for (Task task : tasks.values()) {
                    fileWriter.write(task.toString() + "\n");
                }
            }

            if (!epics.isEmpty()) {
                for (Task epic : epics.values()) {
                    fileWriter.write(epic.toString() + "\n");
                }
            }

            if (!subtasks.isEmpty()) {
                for (Task epic : subtasks.values()) {
                    fileWriter.write(epic.toString() + "\n");
                }
            }

            fileWriter.write("\n");
            fileWriter.write(historyToString());

        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    public void read() {
        try (Reader fileReader = new FileReader(file); BufferedReader br = new BufferedReader(fileReader)) {

            if (br.ready()) {
                br.readLine();
            }

            List<String[]> epicInfo = new ArrayList<>();

            while (br.ready()) {
                String rd = br.readLine();

                String[] line;

                if (rd.equals("")) {
                    break;
                }

                line = rd.split(",");

                if (Type.valueOf(line[1]) == Type.EPIC) {
                    epicInfo.add(line);
                } else {
                    fromString(line);
                }
            }

            for (String[] valueOfEpic : epicInfo) {
                makeEpicWithString(valueOfEpic);
            }

            if (br.ready()) {
                String rd = br.readLine();
                String[] line = rd.split(",");

                readHistory(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readHistory(String[] value) {
        for (int i = value.length - 1; i > -1; i--) {

            if (tasks.containsKey(Long.parseLong(value[i]))) {
                historyManager.add(tasks.get(Long.parseLong(value[i])));
            }
            if (epics.containsKey(Long.parseLong(value[i]))) {
                historyManager.add(epics.get(Long.parseLong(value[i])));
            }
            if (subtasks.containsKey(Long.parseLong(value[i]))) {
                historyManager.add(subtasks.get(Long.parseLong(value[i])));
            }

        }

    }

    private String historyToString() {
        List<Task> history;
        StringBuilder historyInNum = new StringBuilder("null");

        if (historyManager != null) {
            history = new ArrayList<>(historyManager.getHistory());

            if (!history.isEmpty()) {
                historyInNum = new StringBuilder();

                for (int i = 0; i < history.size() - 1; i++) {
                    historyInNum.append(history.get(i).getTaskId()).append(",");
                }

                historyInNum.append(history.get(history.size() - 1).getTaskId());
            }

        }
        return historyInNum.toString();
    }

    private void fromString(String[] value) {

        switch (Type.valueOf(value[1])) {
            case TASK:
                addAnyTypeOfTask(new Task(Long.parseLong(value[0]), value[2], value[4], Status.valueOf(value[3]),
                        Duration.ofSeconds(Long.parseLong(value[5])), LocalDateTime.parse(value[6], Task.dateTimeFormatter)));                                                                  //!!!!!
                break;
            case SUBTASK:
                addAnyTypeOfTask(new SubTask(Long.parseLong(value[0]), value[2], value[4], Status.valueOf(value[3]),
                        Duration.ofSeconds(Long.parseLong(value[5])),
                        LocalDateTime.parse(value[6], Task.dateTimeFormatter)));
        }

    }

    private void makeEpicWithString(String[] value) {
        ArrayList<SubTask> subTasks = null;

        if (!value[5].equals("null")) {
            subTasks = new ArrayList<>();
            for (int i = 5; i < value.length; i++) {
                subTasks.add(subtasks.get(Long.valueOf(value[i])));
            }
        }

        addAnyTypeOfTask(new Epic(Long.parseLong(value[0]), value[2], value[4], Status.valueOf(value[3]), subTasks));                                                                          //!!!!!!!!
    }
}
