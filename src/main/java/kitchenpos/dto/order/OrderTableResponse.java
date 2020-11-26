package kitchenpos.dto.order;

import kitchenpos.domain.order.OrderTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderTableResponse {
    private Long id;
    private Integer numberOfGuests;
    private Boolean empty;

    public OrderTableResponse(OrderTable orderTable) {
        this.id = orderTable.getId();
        this.numberOfGuests = orderTable.getNumberOfGuests();
        this.empty = orderTable.getEmpty();
    }
}
