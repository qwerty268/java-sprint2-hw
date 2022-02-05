package tracker.service.manager;

import tracker.model.Task;
import java.util.List;

public interface HistoryManager {

    public void add(Task task);

    public void remove(long id);

    public List<Task> getHistory();

}
