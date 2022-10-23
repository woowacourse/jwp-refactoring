package kitchenpos.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import lombok.Getter;

@Getter
public class OrderDto {

    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItem> orderLineItems;

    public OrderDto(Long id,
                    Long orderTableId,
                    String orderStatus,
                    LocalDateTime orderedTime,
                    List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderDto of(Order order, List<OrderLineItem> orderLineItems) {
        Long id = order.getId();
        Long orderTableId = order.getOrderTableId();
        String orderStatus = order.getOrderStatus();
        LocalDateTime orderedTime = order.getOrderedTime();
        return new OrderDto(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }
}
