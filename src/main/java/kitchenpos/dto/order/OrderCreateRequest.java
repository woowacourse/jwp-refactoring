package kitchenpos.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {
    @NotNull
    private Long orderTableId;

    @NotEmpty
    private List<OrderLineItemRequest> orderLineItems;
}
