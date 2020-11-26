package kitchenpos.dto.order;

import kitchenpos.domain.order.OrderTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderTableCreateRequest {

    @Min(1)
    @NotNull
    private Integer numberOfGuests;

    @NotNull
    private Boolean empty;

    public OrderTable toOrderTable() {
        return new OrderTable(numberOfGuests, empty);
    }
}
