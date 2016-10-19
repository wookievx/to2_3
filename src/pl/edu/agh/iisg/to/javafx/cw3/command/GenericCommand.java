package pl.edu.agh.iisg.to.javafx.cw3.command;


public class GenericCommand implements Command {

    private final Runnable actionDo;
    private final Runnable actionUndo;

    public GenericCommand(Runnable actionDo, Runnable actionUndo) {
        this.actionDo = actionDo;
        this.actionUndo = actionUndo;
    }

    @Override
    public void execute() {
        actionDo.run();
    }

    @Override
    public void undo() {
        actionUndo.run();
    }

    @Override
    public void redo() {
        actionDo.run();
    }

    @Override
    public String getName() {
        return getClass().getName();
    }
}
