package kitchenpos.order.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;
}
