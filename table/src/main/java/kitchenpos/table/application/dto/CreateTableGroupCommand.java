package kitchenpos.table.application.dto;

import static kitchenpos.table.exception.TableGroupExceptionType.ORDER_TABLE_IDS_CAN_NOT_NULL;

import java.util.List;
import java.util.Objects;
import kitchenpos.table.exception.TableGroupException;

public class CreateTableGroupCommand {

    private final List<Long> orderTableIds;

    public CreateTableGroupCommand(List<Long> orderTableIds) {
        if (Objects.isNull(orderTableIds)) {
            throw new TableGroupException(ORDER_TABLE_IDS_CAN_NOT_NULL);
        }
        this.orderTableIds = orderTableIds;
    }

    public List<Long> orderTableIds() {
        return orderTableIds;
    }
}
