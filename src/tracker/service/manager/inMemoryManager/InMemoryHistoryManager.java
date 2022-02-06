package tracker.service.manager.inMemoryManager;

import tracker.model.Task;
import tracker.service.manager.HistoryManager;
import tracker.service.manager.inMemoryManager.list.LinkedList;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private LinkedList linkedList= new LinkedList();

    @Override
    public void add(Task task) {
    linkedList.linkLast(task);
    }

    @Override
    public void remove(long id) {
    linkedList.removeNode(id);
    }

    @Override
    public List<Task> getHistory() {
       return linkedList.getTasks();
    }
}
