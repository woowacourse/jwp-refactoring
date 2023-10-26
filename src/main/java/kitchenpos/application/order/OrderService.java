package kitchenpos.application.order;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.dto.ChangeOrderStatusRequest;
import kitchenpos.dto.OrderRequest;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderValidator orderValidator;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderMapper orderMapper, final OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public Order create(final OrderRequest request) {
        final Order order = orderMapper.toOrder(request, orderValidator);
        return orderRepository.save(order);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("없는 주문이에요."));

        final OrderStatus orderStatus = request.getOrderStatus();
        savedOrder.changeStatus(orderStatus);
        return savedOrder;
    }
}
