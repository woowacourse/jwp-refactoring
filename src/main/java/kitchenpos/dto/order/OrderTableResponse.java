package kitchenpos.dto.order;

import kitchenpos.domain.order.OrderTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderTableResponse {
    private Long id;
    private Integer numberOfGuests;
    private Boolean empty;
    private TableGroupResponse tableGroup;

    public OrderTableResponse(OrderTable orderTable) {
        this.id = orderTable.getId();
        this.numberOfGuests = orderTable.getNumberOfGuests();
        this.empty = orderTable.getEmpty();
        if (Objects.nonNull(orderTable.getTableGroup())) {
            this.tableGroup = new TableGroupResponse(orderTable.getTableGroup());
        }
    }
}
