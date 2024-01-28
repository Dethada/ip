package cappy.parser;

import java.util.List;
import java.util.HashMap;
import java.io.IOException;

import cappy.command.Command;
import cappy.command.CommandType;
import cappy.storage.Storage;
import cappy.task.TaskList;
import cappy.ui.Ui;
import cappy.error.CappyException;

public class ParsedInput {
    private CommandType commandType;
    private Command command;
    private HashMap<String, String> namedArguments;
    private List<String> positionalArguments;

    public ParsedInput(CommandType commandType, HashMap<String, String> namedArguments, List<String> positionalArguments) {
        this.commandType = commandType;
        this.command = commandType.getCommand();
        this.namedArguments = namedArguments;
        this.positionalArguments = positionalArguments;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public String getNamedArgument(String name) {
        return namedArguments.get(name);
    }

    public String getPositionalArgument(int index) {
        return positionalArguments.get(index);
    }

    public int numberOfPositionalArguments() {
        return positionalArguments.size();
    }

    public int numberOfNamedArguments() {
        return namedArguments.size();
    }

    public boolean hasNamedArgument(String name) {
        return namedArguments.containsKey(name);
    }

    public void executeCommand(TaskList tasks, Ui ui, Storage storage) throws CappyException, IOException {
        command.execute(tasks, ui, storage, this);
    }
}