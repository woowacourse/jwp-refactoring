package kitchenpos.order;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderCreateValidator orderCreateValidator;

    public OrderService(
            final OrderRepository orderRepository,
            OrderCreateValidator orderCreateValidator) {
        this.orderRepository = orderRepository;
        this.orderCreateValidator = orderCreateValidator;
    }

    @Transactional
    public Order create(final Order order) {
        orderCreateValidator.validate(order);

        return orderRepository.save(order);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));

        savedOrder.changeOrderStatus(orderStatus);

        return savedOrder;
    }
}
