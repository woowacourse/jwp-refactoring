package kitchenpos.ui.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.Order;

public class CreateOrderResponse {

    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<CreateOrderLineItemResponse> orderLineItems;

    public CreateOrderResponse(final Order order) {
        this.id = order.getId();
        this.orderTableId = order.getOrderTableId();
        this.orderStatus = order.getOrderStatus().name();
        this.orderedTime = order.getOrderedTime();
        this.orderLineItems = convertCreateOrderLineItems(order);
    }

    private List<CreateOrderLineItemResponse> convertCreateOrderLineItems(final Order order) {
        return order.getOrderLineItems()
                    .stream()
                    .map(CreateOrderLineItemResponse::new)
                    .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<CreateOrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
