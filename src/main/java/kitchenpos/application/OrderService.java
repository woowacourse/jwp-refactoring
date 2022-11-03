package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.request.OrderCreateRequest;
import kitchenpos.application.request.OrderUpdateRequest;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(final OrderRepository orderRepository, final OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public Order create(final OrderCreateRequest request) {
        return orderRepository.save(Order.create(request.getOrderTableId(),
                OrderStatus.COOKING,
                createOrderLineItems(request),
                orderValidator));
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderUpdateRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Order 입니다."));

        savedOrder.changeStatus(OrderStatus.valueOf(request.getOrderStatus()));
        return savedOrder;
    }

    private List<OrderLineItem> createOrderLineItems(final OrderCreateRequest request) {
        return request.getOrderLineItems()
                .stream()
                .map(it -> new OrderLineItem(it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
    }
}
