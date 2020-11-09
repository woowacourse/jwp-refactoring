package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.command.ChangeOrderStatusCommand;
import kitchenpos.application.command.CreateOrderCommand;
import kitchenpos.application.response.OrderResponse;
import kitchenpos.domain.model.order.Order;
import kitchenpos.domain.model.order.OrderCreateService;
import kitchenpos.domain.model.order.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderCreateService orderCreateService;

    public OrderService(OrderRepository orderRepository, OrderCreateService orderCreateService) {
        this.orderRepository = orderRepository;
        this.orderCreateService = orderCreateService;
    }

    @Transactional
    public OrderResponse create(final CreateOrderCommand command) {
        Order order = command.toEntity();
        Order saved = orderRepository.save(order.create(orderCreateService));
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
