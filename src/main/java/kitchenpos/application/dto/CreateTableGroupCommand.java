package kitchenpos.application.dto;

import java.util.List;

public class CreateTableGroupCommand {

    private final List<Long> orderTableIds;

    public CreateTableGroupCommand(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> orderTableIds() {
        return orderTableIds;
    }
}
