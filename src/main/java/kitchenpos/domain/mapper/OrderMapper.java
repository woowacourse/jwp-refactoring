package kitchenpos.domain.mapper;

import kitchenpos.application.dto.request.CreateOrderRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    private final OrderTableRepository orderTableRepository;

    private OrderMapper(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public Order toOrder(CreateOrderRequest request, List<OrderLineItem> orderLineItems) {
        return Order.builder()
                .orderTable(getOrderTable(request.getOrderTableId()))
                .orderStatus(OrderStatus.valueOf(request.getOrderStatus()))
                .orderedTime(request.getOrderedTime())
                .orderLineItems(orderLineItems)
                .build();
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
