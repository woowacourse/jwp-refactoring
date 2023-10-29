package kitchenpos.order.service;

import java.util.List;
import kitchenpos.order.domain.MenuDetailsSnapshotEvent;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.service.dto.OrderRequest;
import kitchenpos.order.service.dto.OrderResponse;
import kitchenpos.order.service.dto.OrderStatusRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final ApplicationEventPublisher publisher;

    public OrderService(final OrderRepository orderRepository, final OrderValidator orderValidator,
                        final ApplicationEventPublisher publisher) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.publisher = publisher;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        final Order order = OrderMapper.toOrder(request);
        order.place(orderValidator);
        publisher.publishEvent(new MenuDetailsSnapshotEvent(order));
        orderRepository.save(order);
        return OrderResponse.from(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return OrderResponse.from(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Order order = findOrderById(orderId);
        order.updateStatus(OrderStatus.valueOf(request.getOrderStatus()));
        return OrderResponse.from(order);
    }

    private Order findOrderById(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
    }
}
