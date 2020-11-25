package kitchenpos.application.command;

public class ChangeOrderTableEmptyCommand {
    private boolean empty;

    private ChangeOrderTableEmptyCommand() {
    }

    public ChangeOrderTableEmptyCommand(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
