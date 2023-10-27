package kitchenpos.application.order;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderValidator;
import kitchenpos.dto.OrderRequest;

@Component
public class OrderMapper {

    public Order toOrder(final OrderRequest orderRequest, final OrderValidator orderValidator) {
        final List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItemRequests().stream()
                .map(orderLineItemRequest -> new OrderLineItem(
                        orderLineItemRequest.getMenuId(),
                        orderLineItemRequest.getQuantity()))
                .collect(Collectors.toList());
        return Order.of(orderRequest.getOrderTableId(), new OrderLineItems(orderLineItems), orderValidator);
    }
}
