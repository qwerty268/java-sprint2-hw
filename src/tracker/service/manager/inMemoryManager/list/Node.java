package tracker.service.manager.inMemoryManager.list;

import tracker.model.Task;

public class Node {
    public Node previous;
    public Node next;
    public Task task;

    public Node(Node previous, Node next, Task task) {
        this.previous = previous;
        this.next = next;
        this.task = task;
    }
}
