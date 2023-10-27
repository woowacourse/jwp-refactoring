package kitchenpos.application.table.dto;

public class ChangeOrderTableEmptyCommand {

    private final Long id;
    private final boolean empty;

    public ChangeOrderTableEmptyCommand(Long id, boolean empty) {
        this.id = id;
        this.empty = empty;
    }

    public Long id() {
        return id;
    }

    public boolean empty() {
        return empty;
    }
}
