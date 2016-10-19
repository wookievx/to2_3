package pl.edu.agh.iisg.to.javafx.cw3.command;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CommandRegistry {

	private ObservableList<Command> commandStack = FXCollections
			.observableArrayList();
	private ObservableList<Command> undoneStack = FXCollections.observableArrayList();

	public void executeCommand(Command command) {
        undoneStack.clear();
		command.execute();
		commandStack.add(command);
	}

	public void redo() {
        if (!undoneStack.isEmpty()) {
            Command command = undoneStack.get(undoneStack.size() - 1);
            command.redo();
            commandStack.add(command);
            undoneStack.remove(undoneStack.size() - 1);
        }
	}

	public void undo() {
        if (!commandStack.isEmpty()) {
            Command command = commandStack.get(commandStack.size() - 1);
            command.undo();
            undoneStack.add(command);
            commandStack.remove(commandStack.size() - 1);
        }
	}

	public ObservableList<Command> getCommandStack() {
		return commandStack;
	}

    public ObservableList<Command> getUndoneStack() {
        return undoneStack;
    }
}
