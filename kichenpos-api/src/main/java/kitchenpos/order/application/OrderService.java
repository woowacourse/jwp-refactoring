package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.ChangeOrderStatusDto;
import kitchenpos.order.dto.CreateOrderDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderValidator orderValidator
    ) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public Order create(final CreateOrderDto request) {
        final Order order = Order.createNewOrder(request.getOrderTableId(), request.toOrderLineItems(), orderValidator);

        return orderRepository.save(order);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final ChangeOrderStatusDto request) {
        final Order savedOrder = orderRepository.findById(orderId)
                                                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(request.getOrderStatus());

        return savedOrder;
    }
}
