package tracker.service.manager.inMemoryManager.list;

import tracker.model.Epic;
import tracker.model.Task;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class LinkedList {
    private Map<Long, Node> nodes = new HashMap<>();
    private Node previousNode;
    private Node lastNode;  /* используется. строка 54 . Возможно ты имел ввиду firstNode (его я удалил)*/


    public void linkLast(Task task) {
        Node newNode = new Node(previousNode, null, task);
        lastNode = newNode;

        //проверка на наличие повторяющихся задач
        if (nodes.get(task.getTaskId()) != null) {
            this.removeNode(newNode.task.getTaskId());
        }

        if (nodes.isEmpty()) {
            nodes.put(task.getTaskId(), newNode);
            newNode.previous = null;
            previousNode = newNode;
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

        // если lastNode заменить на previousNode, то в методе linkedLast, при создании нового узла, за место предыдущего будет передан null
        if (lastNode == node) {
            lastNode = node.previous;
            previousNode = lastNode;
        }

        if (node.task.getClass() == Epic.class) {
            for (Task subTask : ((Epic) node.task).getSubTasks()) {
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
