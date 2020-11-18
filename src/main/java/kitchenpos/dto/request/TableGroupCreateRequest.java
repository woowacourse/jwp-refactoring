package kitchenpos.dto.request;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import kitchenpos.validator.OrderTableCountValidate;

public class TableGroupCreateRequest {

    @NotNull
    @Size(min = 2, message = "Table group request must contain 2 or more tables.")
    @OrderTableCountValidate
    private final List<@Valid OrderTableId> orderTables;

    @JsonCreator
    public TableGroupCreateRequest(List<OrderTableId> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
            .map(OrderTableId::getId)
            .collect(Collectors.toList());
    }

    public List<OrderTableId> getOrderTables() {
        return orderTables;
    }
}
