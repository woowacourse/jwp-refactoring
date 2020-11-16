package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.command.ChangeOrderStatusCommand;
import kitchenpos.application.command.CreateOrderCommand;
import kitchenpos.application.response.OrderResponse;
import kitchenpos.domain.model.order.CreateOrderVerifier;
import kitchenpos.domain.model.order.Order;
import kitchenpos.domain.model.order.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CreateOrderVerifier createOrderVerifier;

    public OrderService(OrderRepository orderRepository, CreateOrderVerifier createOrderVerifier) {
        this.orderRepository = orderRepository;
        this.createOrderVerifier = createOrderVerifier;
    }

    @Transactional
    public OrderResponse create(final CreateOrderCommand command) {
        Order order = createOrderVerifier.toOrder(command.getOrderLineItems(),
                command.getOrderTableId());
        Order saved = orderRepository.save(order.create());
        return OrderResponse.of(saved);
    }

    public List<OrderResponse> list() {
        return OrderResponse.listOf(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId,
            final ChangeOrderStatusCommand command) {
        final Order saved = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        Order changed = saved.changeOrderStatus(command.getOrderStatus());
        return OrderResponse.of(orderRepository.save(changed));
    }
}
