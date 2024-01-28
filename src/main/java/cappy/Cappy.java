package cappy;

import java.util.Scanner;
import java.io.IOException;

import cappy.parser.Parser;
import cappy.parser.ParsedInput;
import cappy.command.CommandType;
import cappy.storage.Storage;
import cappy.task.TaskList;
import cappy.ui.Ui;
import cappy.error.CappyException;

public class Cappy {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static TaskList TASKS;
    private static final String STORAGE_PATH = "./cappy.csv";
    private static final Ui UI = new Ui();
    private static Storage storage;

    public static void main(String[] args) {
        UI.showBanner();
        UI.showGreetings();
        try {
            storage = new Storage(STORAGE_PATH);
            TASKS = TaskList.load(storage);
            inputLoop();
            storage.close();
        } catch (IOException e) {
            UI.showError(e.getMessage());
            Cappy.SCANNER.close();
            System.exit(1);
        } catch(CappyException e) {
            UI.showError(e.getMessage());
            Cappy.SCANNER.close();
            System.exit(1);
        } finally {
            Cappy.SCANNER.close();
        }
    }

    private static void inputLoop() {
        String input = "";
        while (true) {
            input = SCANNER.nextLine();
            try {
                ParsedInput parsedInput = Parser.parse(input);
                parsedInput.executeCommand(TASKS, UI, storage);
                if (parsedInput.getCommandType() == CommandType.BYE) {
                    break;
                }
            } catch (CappyException e) {
                UI.showError(e.getMessage());
            } catch (IOException e) {
                UI.showError(e.getMessage());
            }
        }
    }
}