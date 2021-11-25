package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final OrderValidator orderValidator;
    private final OrderRepository orderRepository;

    public OrderService(
            OrderValidator orderValidator,
            OrderRepository orderRepository) {
        this.orderValidator = orderValidator;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order create(final Order order) {
        order.getOrderLineItems().connectOrder(order);
        order.initId();
        order.startOrder(orderValidator);
        return orderRepository.save(order);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        savedOrder.changeOrderStatus(orderValidator, orderStatus.name());
        return savedOrder;
    }
}
