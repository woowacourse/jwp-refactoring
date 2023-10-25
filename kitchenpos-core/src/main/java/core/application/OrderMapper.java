package core.application;

import core.application.dto.OrderRequest;
import core.domain.Order;
import core.domain.OrderLineItem;
import core.domain.OrderValidator;
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
