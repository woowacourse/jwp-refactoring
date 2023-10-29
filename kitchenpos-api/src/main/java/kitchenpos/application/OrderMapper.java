package kitchenpos.application;

import kitchenpos.application.dto.OrderRequest;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderValidator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public Order toOrder(OrderRequest orderRequest, OrderValidator orderValidator) {
        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItems().stream()
                .map(it -> new OrderLineItem(it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
        return Order.createWithoutId(orderRequest.getOrderTableId(), orderLineItems, orderValidator);
    }
}
