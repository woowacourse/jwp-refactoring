package kitchenpos.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.dto.response.OrderLineItemResponse;
import kitchenpos.dto.response.OrderResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoMapperImpl implements OrderDtoMapper {

    @Override
    public OrderResponse toOrderResponse(final Order order) {
        List<OrderLineItemResponse> orderLineItemRespons = order.getOrderLineItems()
                .stream()
                .map(this::toOrderLineItemResponse)
                .collect(Collectors.toList());
        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus().name(),
                order.getOrderedTime(), orderLineItemRespons);
    }

    private OrderLineItemResponse toOrderLineItemResponse(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getMenuId(),
                orderLineItem.getQuantity());
    }

    @Override
    public List<OrderResponse> toOrderResponses(final List<Order> orders) {
        return orders.stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
    }
}
