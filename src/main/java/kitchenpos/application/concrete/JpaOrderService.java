package kitchenpos.application.concrete;

import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderMapper;
import kitchenpos.domain.OrderStatus;
import kitchenpos.repository.OrderRepository;
import kitchenpos.ui.dto.request.OrderChangeStatusRequest;
import kitchenpos.ui.dto.request.OrderCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class JpaOrderService implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    public JpaOrderService(final OrderMapper orderMapper, final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    @Override
    public Order create(final OrderCreateRequest request) {
        final var order = orderMapper.mapFrom(request);

        return orderRepository.save(order);
    }

    @Override
    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    @Override
    public Order changeOrderStatus(final Long orderId, final OrderChangeStatusRequest request) {
        final var orderStatus = OrderStatus.from(request.getOrderStatus());
        final var order = orderRepository.getById(orderId);

        return order.changeOrderStatus(orderStatus);
    }
}
