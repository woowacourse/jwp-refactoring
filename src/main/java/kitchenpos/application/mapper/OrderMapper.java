package kitchenpos.application.mapper;

import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.application.dto.OrderRequest.OrderLineItemRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.repository.OrderTableRepository;
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
