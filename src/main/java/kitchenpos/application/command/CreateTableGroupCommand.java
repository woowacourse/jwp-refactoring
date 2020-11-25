package kitchenpos.application.command;

import java.util.HashSet;
import java.util.List;

import javax.validation.constraints.Size;

public class CreateTableGroupCommand {
    @Size(min = 2)
    private List<Long> orderTables;

    private CreateTableGroupCommand() {
    }

    public CreateTableGroupCommand(List<Long> orderTables) {
        this.orderTables = orderTables;
        validate(this.orderTables);
    }

    private void validate(List<Long> orderTables) {
        if (hasDuplicatedItem(orderTables)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean hasDuplicatedItem(List<Long> orderTables) {
        return !orderTables.stream()
                .allMatch(new HashSet<>()::add);
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
