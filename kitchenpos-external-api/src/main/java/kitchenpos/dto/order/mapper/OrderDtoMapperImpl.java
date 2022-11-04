package kitchenpos.dto.order.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.dto.order.response.OrderLineItemResponse;
import kitchenpos.dto.order.response.OrderResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoMapperImpl implements OrderDtoMapper {

    @Override
    public OrderResponse toOrderResponse(final Order order) {
        List<OrderLineItemResponse> orderLineItemResponses = order.getOrderLineItems()
                .stream()
                .map(this::toOrderLineItemResponse)
                .collect(Collectors.toList());
        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus().name(),
                order.getOrderedTime(), orderLineItemResponses);
    }

    private OrderLineItemResponse toOrderLineItemResponse(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getQuantity(),
                orderLineItem.getMenuName(), orderLineItem.getMenuPrice());
    }

    @Override
    public List<OrderResponse> toOrderResponses(final List<Order> orders) {
        return orders.stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
    }
}
