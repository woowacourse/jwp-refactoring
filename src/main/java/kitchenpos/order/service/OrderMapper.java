package kitchenpos.order.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.service.OrderRequest.OrderLineItemRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    private final OrderTableRepository orderTableRepository;

    public OrderMapper(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public Order mapFrom(OrderRequest orderRequest) {
        return new Order(
                findOrderTableById(orderRequest.getOrderTableId()),
                orderLineItemList(orderRequest.getOrderLineItems())
        );
    }

    private OrderTable findOrderTableById(Long tableId) {
        return orderTableRepository.findById(tableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<OrderLineItem> orderLineItemList(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream().map(this::toOrderLineItem).collect(Collectors.toList());
    }

    private OrderLineItem toOrderLineItem(OrderLineItemRequest orderLineItemRequest) {
        return new OrderLineItem(
                orderLineItemRequest.getMenuId(),
                orderLineItemRequest.getQuantity());
    }
}
