package cappy.task;

import cappy.error.CappyException;
import cappy.parser.Parser;
import cappy.storage.Storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of tasks in the task management system.
 *
 * <p>The {@code TaskList} class manages a list of tasks and provides operations such as adding,
 * retrieving, removing tasks, and saving/loading tasks to/from storage. Each task is associated
 * with a 1-based index within the task list.
 */
public class TaskList {
    private final List<Task> tasks;
    private final Storage storage;

    private TaskList() {
        this.tasks = new ArrayList<>();
        storage = null;
    }

    private TaskList(Storage storage) {
        this.tasks = new ArrayList<>();
        this.storage = storage;
    }

    /**
     * Adds a task to this task list.
     *
     * @param task The task to be added.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at the specified 1-based index.
     *
     * @param index 1-based index of the task to return.
     * @return the task at the specified 1-based index.
     */
    public Task getTask(int index) {
        return tasks.get(index - 1);
    }

    /**
     * Removes the task at the specified 1-based index.
     *
     * @param index 1-based index of the task to remove.
     */
    public void removeTask(int index) {
        tasks.remove(index - 1);
    }

    /** Returns the number of tasks in this task list. */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns true if the specified index is valid.
     *
     * @param index 1-based index to check.
     * @return true if the specified index is valid.
     */
    public boolean validIndex(int index) {
        return index >= 1 && index <= tasks.size();
    }

    /**
     * Saves the current state of the task list to storage.
     *
     * @throws IOException If any I/O Error is encountered when saving.
     */
    public void save() throws IOException {
        if (storage != null) {
            storage.empty();
            for (Task task : tasks) {
                storage.writeLine(task.toCsv());
            }
        }
    }

    /**
     * Creates a new TaskList using the data stored in the storage.
     *
     * @param storage The storage to load data from.
     * @return The newly created TaskList.
     */
    public static TaskList load(Storage storage) throws IOException, CappyException {
        TaskList taskList = new TaskList(storage);
        String[] csvLines = storage.readAll().split("\n");
        for (String line : csvLines) {
            if (!line.equals("")) {
                taskList.addTask(Parser.parseCsvLine(line));
            }
        }
        return taskList;
    }

    public TaskList search(String keyword) {
        TaskList taskList = new TaskList();
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                taskList.addTask(task);
            }
        }
        return taskList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1).append(". ").append(tasks.get(i).toString()).append("\n");
        }
        return sb.toString();
    }
}
