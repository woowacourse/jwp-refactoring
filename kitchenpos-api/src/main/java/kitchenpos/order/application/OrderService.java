package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.application.dto.request.OrderCommand;
import kitchenpos.order.application.dto.request.OrderStatusCommand;
import kitchenpos.order.application.dto.response.OrderResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(final OrderRepository orderRepository, final OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    public OrderResponse create(final OrderCommand orderCommand) {
        Order order = Order.create(orderCommand.orderTableId(), orderCommand.toOrderLineItems(), orderValidator);
        return OrderResponse.from(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .toList();
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusCommand orderStatusCommand) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
        savedOrder.changeOrderStatus(OrderStatus.from(orderStatusCommand.orderStatus()));

        return OrderResponse.from(savedOrder);
    }
}
