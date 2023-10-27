package kitchenpos.order;

import kitchenpos.request.OrderCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderCreateValidator orderCreateValidator;
    private final MenuHistoryRecorder menuHistoryRecorder;

    public OrderService(
            final OrderRepository orderRepository,
            OrderCreateValidator orderCreateValidator,
            MenuHistoryRecorder menuHistoryRecorder
    ) {
        this.orderRepository = orderRepository;
        this.orderCreateValidator = orderCreateValidator;
        this.menuHistoryRecorder = menuHistoryRecorder;
    }

    @Transactional
    public Order create(OrderCreateRequest request) {
        List<OrderLineItem> orderLineItems = request.getOrderLineItemRequests().stream()
                .map(orderLineItemRequest -> new OrderLineItem(
                        orderLineItemRequest.getMenuId(),
                        orderLineItemRequest.getQuantity(),
                        menuHistoryRecorder))
                .collect(Collectors.toList());

        Order order = new Order(orderLineItems, request.getOrderTableId());

        orderCreateValidator.validate(order);

        return orderRepository.save(order);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(Long orderId, OrderStatus orderStatus) {
        Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));

        savedOrder.changeOrderStatus(orderStatus);

        return savedOrder;
    }
}
