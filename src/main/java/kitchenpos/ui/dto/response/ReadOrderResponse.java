package kitchenpos.ui.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;

public class ReadOrderResponse {

    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<ReadOrderLineItemResponse> orderLineItems;

    public ReadOrderResponse(final Order order) {
        this.id = order.getId();
        this.orderTableId = order.getOrderTable().getId();
        this.orderStatus = order.getOrderStatus().name();
        this.orderedTime = order.getOrderedTime();
        this.orderLineItems = convertReadOrderLineItems(order);
    }

    private List<ReadOrderLineItemResponse> convertReadOrderLineItems(final Order order) {
        return order.getOrderLineItems()
                    .stream()
                    .map(ReadOrderLineItemResponse::new)
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

    public List<ReadOrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
