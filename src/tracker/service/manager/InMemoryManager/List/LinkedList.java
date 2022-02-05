package tracker.service.manager.InMemoryManager.List;

import tracker.model.Epic;
import tracker.model.SubTask;
import tracker.model.Task;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class LinkedList {
    private Map<Long, Node> nodes = new HashMap<>();
    private Node previousNode;
    private Node firstNode;
    private Node lastNode;


    public void linkLast(Task task) {
        Node newNode = new Node(previousNode, null, task);
        lastNode = newNode;

        //проверка на наличие повторяющихся задач
        if (nodes.get(task.getTaskId()) != null){
            this.removeNode(newNode.task.getTaskId());
        }

        if (nodes.isEmpty()) {
            nodes.put(task.getTaskId(), newNode);
            previousNode = newNode;
            firstNode = newNode;
        } else {
            nodes.put(task.getTaskId(), newNode);
            previousNode.next = newNode;
            previousNode = newNode;
        }
    }


    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node node = lastNode;

        while (node != null) {
            tasks.add(node.task);
            node = node.previous;
        }
        return tasks;
    }

    public void removeNode(long id) {
        Node node = nodes.get(id);
        nodes.remove(id);

        if (lastNode == node){
            lastNode = null;
        }

        if (node.task.getClass() == SubTask.class){
            for (Task subTask: ((Epic) node.task).getSubTasks()) {
                this.removeNode(subTask.getTaskId());
            }
        }

        //замена ссылок на узлы в соседних нодах
        if (node.previous != null) {
            node.previous.next = node.next;
        }
        if (node.next != null) {
            node.next.previous = node.previous;
        }
    }
}
