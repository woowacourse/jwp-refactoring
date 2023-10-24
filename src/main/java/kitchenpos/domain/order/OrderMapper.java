package kitchenpos.domain.order;

import kitchenpos.application.dto.request.CreateOrderRequest;
import kitchenpos.domain.table.OrderTableRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderMapper {

    private final OrderTableRepository orderTableRepository;

    private OrderMapper(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public Order toOrder(CreateOrderRequest request, List<OrderLineItem> orderLineItems) {
        validate(request.getOrderTableId());
        return Order.builder()
                .orderTableId(request.getOrderTableId())
                .orderLineItems(orderLineItems)
                .orderedTime(LocalDateTime.now())
                .orderStatus(OrderStatus.COOKING)
                .build();
    }

    private void validate(Long orderTableId) {
        if (!orderTableRepository.existsById(orderTableId)) {
            throw new IllegalArgumentException();
        }
    }
}
