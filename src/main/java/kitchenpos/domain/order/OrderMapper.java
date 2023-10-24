package kitchenpos.domain.order;

import kitchenpos.dto.request.CreateOrderRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    private OrderMapper() {
    }

    public Order toOrder(CreateOrderRequest request) {
        return Order.builder()
                .orderTableId(request.getOrderTableId())
                .orderLineItems(getOrderLineItems(request.getOrderLineItems()))
                .orderedTime(LocalDateTime.now())
                .orderStatus(OrderStatus.COOKING)
                .build();
    }

    private List<OrderLineItem> getOrderLineItems(List<CreateOrderRequest.CreateOrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(this::toOrderLineItem)
                .collect(Collectors.toList());
    }

    private OrderLineItem toOrderLineItem(CreateOrderRequest.CreateOrderLineItem request) {
        return OrderLineItem.builder()
                .menuId(request.getMenuId())
                .quantity(request.getQuantity())
                .build();
    }
}
