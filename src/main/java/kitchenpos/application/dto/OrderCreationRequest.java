package kitchenpos.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import org.springframework.util.CollectionUtils;

public class OrderCreationRequest {

    private final Long orderTableId;
    private final List<OrderItemsWithQuantityRequest> orderLineItems;

    @JsonCreator
    public OrderCreationRequest(final Long orderTableId, final List<OrderItemsWithQuantityRequest> orderLineItems) {
        validate(orderLineItems);
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public void validate(List<OrderItemsWithQuantityRequest> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("OrderLineItems must not be empty");
        }
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderItemsWithQuantityRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
