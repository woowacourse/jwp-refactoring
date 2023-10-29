package kitchenpos.order.service;

import static kitchenpos.exception.ExceptionType.ORDER_NOT_FOUND;

import kitchenpos.exception.CustomException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order foundOrder = orderRepository.findById(orderId)
                                                .orElseThrow(() -> new CustomException(ORDER_NOT_FOUND));

        foundOrder.changeOrderStatus(order);

        return foundOrder;
    }
}
