package kitchenpos.application.dto.request;

import java.util.List;

public class TableGroupCommand {

    private final List<Long> orderTableId;

    public TableGroupCommand(List<Long> orderTableId) {
        this.orderTableId = orderTableId;
    }

    public List<Long> getOrderTableId() {
        return orderTableId;
    }
}
