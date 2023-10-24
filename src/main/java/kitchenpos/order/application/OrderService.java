package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.dto.OrderCreationRequest;
import kitchenpos.order.application.dto.OrderResult;
import kitchenpos.order.application.dto.OrderStatusChangeRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final OrderMapper orderMapper;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderValidator orderValidator,
            final OrderMapper orderMapper
    ) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public OrderResult create(final OrderCreationRequest request) {
        final Order order = orderMapper.from(request, orderValidator);
        orderRepository.save(order);
        return OrderResult.from(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResult> list() {
        return orderRepository.findAll().stream()
                .map(OrderResult::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResult changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order existOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        existOrder.changeOrderStatus(request.getOrderStatus());
        return OrderResult.from(existOrder);
    }
}
