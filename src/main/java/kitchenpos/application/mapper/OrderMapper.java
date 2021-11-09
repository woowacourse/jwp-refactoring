package kitchenpos.application.mapper;

import java.util.stream.Collectors;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.service.OrderRequest;
import kitchenpos.order.service.OrderRequest.OrderLineItemRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    private final OrderTableRepository orderTableRepository;

    public OrderMapper(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public Order mapFrom(OrderRequest orderRequest) {
        return new Order(
                orderTableRepository.findById(orderRequest.getOrderTableId())
                        .orElseThrow(IllegalArgumentException::new),
                orderRequest.getOrderLineItems()
                        .stream()
                        .map(this::toOrderLineItem)
                        .collect(Collectors.toList())
        );
    }

    private OrderLineItem toOrderLineItem(OrderLineItemRequest orderLineItemRequest) {
        return new OrderLineItem(
                orderLineItemRequest.getMenuId(),
                orderLineItemRequest.getQuantity());
    }
}
