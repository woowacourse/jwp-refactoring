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
        OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        return new Order(orderTable);
    }

    public List<OrderLineItem> orderLineItemList(OrderRequest orderRequest) {
        return orderRequest.getOrderLineItems().stream().map(this::toOrderLineItem).collect(Collectors.toList());
    }

    private OrderLineItem toOrderLineItem(OrderLineItemRequest orderLineItemRequest) {
        return new OrderLineItem(
                orderLineItemRequest.getMenuId(),
                orderLineItemRequest.getQuantity());
    }
}
