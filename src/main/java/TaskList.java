import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class TaskList {
    private final List<Task> tasks;
    private final Storage storage;

    private TaskList(Storage storage) {
        this.tasks = new ArrayList<>();
        this.storage = storage;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    /**
     * Returns the task at the specified 1-based index.
     *
     * @param index 1-based index of the task to return.
     * @return the task at the specified 1-based index.
     */
    public Task getTask(int index) {
        return this.tasks.get(index - 1);
    }

    /**
     * Removes the task at the specified 1-based index.
     *
     * @param index 1-based index of the task to remove.
     */
    public void removeTask(int index) {
        this.tasks.remove(index - 1);
    }

    public int size() {
        return this.tasks.size();
    }

    /**
     * Returns true if the specified index is valid.
     *
     * @param index 1-based index to check.
     * @return true if the specified index is valid.
     */
    public boolean validIndex(int index) {
        return index >= 1 && index <= this.tasks.size();
    }

    public void save() throws IOException {
        this.storage.empty();
        for (Task task : this.tasks) {
            this.storage.writeLine(task.toCsv());
        }
    }

    public static TaskList load(Storage storage) throws IOException, DukeException {
        TaskList taskList = new TaskList(storage);
        String[] csvLines = storage.readAll().split("\n");
        for (String line : csvLines) {
            if (!line.equals("")) {
                taskList.addTask(Parser.parseCsvLine(line));
            }
        }
        return taskList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.tasks.size(); i++) {
            sb.append(i + 1).append(". ").append(this.tasks.get(i).toString()).append("\n");
        }
        return sb.toString();
    }
}
