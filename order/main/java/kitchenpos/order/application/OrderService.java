package order.main.java.kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.PlaceOrderService;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final PlaceOrderService placeOrderService;

    public OrderService(
            final OrderRepository orderRepository,
            final PlaceOrderService placeOrderService) {
        this.orderRepository = orderRepository;
        this.placeOrderService = placeOrderService;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        final List<OrderLineItem> orderLineItems = createOrderLineItemsByOrderRequest(orderRequest);
        final Order order = Order.of(orderRequest.getOrderTableId(), orderLineItems, placeOrderService);
        return orderRepository.save(order);
    }

    private static List<OrderLineItem> createOrderLineItemsByOrderRequest(final OrderRequest orderRequest) {
        return orderRequest.getOrderLineItems().stream()
                .map(orderLineItemRequest -> new OrderLineItem(orderLineItemRequest.getMenuId(),
                        orderLineItemRequest.getQuantity()))
                .collect(Collectors.toList());
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
        savedOrder.changeStatus(OrderStatus.valueOf(request.getOrderStatus()));
        return savedOrder;
    }
}
