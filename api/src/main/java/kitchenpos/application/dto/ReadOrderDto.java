package kitchenpos.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;

public class ReadOrderDto {

    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<ReadOrderLineItemDto> orderLineItems;

    public ReadOrderDto(final Order order) {
        this.id = order.getId();
        this.orderTableId = order.getOrderTableId();
        this.orderStatus = order.getOrderStatus();
        this.orderedTime = order.getOrderedTime();
        this.orderLineItems = convertOrderLineItemDtos(order);
    }

    private List<ReadOrderLineItemDto> convertOrderLineItemDtos(final Order order) {
        return order.getOrderLineItems()
                    .stream()
                    .map(ReadOrderLineItemDto::new)
                    .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<ReadOrderLineItemDto> getOrderLineItems() {
        return orderLineItems;
    }
}
