package kitchenpos.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.order.CreateOrderCommand;
import kitchenpos.application.dto.orderlineitem.OrderLineItemCommand;

public class CreateOrderRequest {

    @JsonProperty("orderTableId")
    private Long orderTableId;
    @JsonProperty("orderLineItems")
    private List<OrderLineItemRequest> orderLineItemRequests;

    public CreateOrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public CreateOrderCommand toCommand() {
        return new CreateOrderCommand(
                orderTableId,
                orderLineItemRequests.stream()
                        .map(it -> new OrderLineItemCommand(it.menuId(), it.quantity()))
                        .collect(Collectors.toList())
        );
    }
}
