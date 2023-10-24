package kitchenpos.application.dto.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;

public class OrderResponse {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<MenuQuantityDto> orderLineItems;

    private OrderResponse() {}

    private OrderResponse(final Long id, final Long orderTableId, final String orderStatus,
                         final LocalDateTime orderedTime,
                         final List<MenuQuantityDto> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(final Order order) {
        final List<MenuQuantityDto> menuQuantityDtos = order.getOrderLineItems()
            .stream()
            .map(MenuQuantityDto::from)
            .collect(Collectors.toList());

        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus(), order.getOrderedTime(), menuQuantityDtos);
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

    public List<MenuQuantityDto> getOrderLineItems() {
        return orderLineItems;
    }
}
