package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.OrderStatusRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderValidator orderValidator) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    public Long order(final OrderCreateRequest request) {
        orderValidator.validate(request.getOrderTableId());
        final List<OrderLineItem> orderLineItems = createOrderLineItems(request);
        final Order order = Order.ofCooking(request.getOrderTableId(), orderLineItems);
        final Order saveOrder = orderRepository.save(order);
        return saveOrder.getId();
    }

    private List<OrderLineItem> createOrderLineItems(final OrderCreateRequest request) {
        return request.getOrderLineItems().stream()
                .map(orderLineItemRequest -> {
                    final Menu menu = menuRepository.getById(orderLineItemRequest.getMenuId());
                    return OrderLineItem.of(menu.getId(), orderLineItemRequest.getQuantity());
                }).collect(Collectors.toUnmodifiableList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderRepository.getById(orderId);
        savedOrder.updateOrderStatus(orderStatusRequest.getOrderStatus());
        return OrderResponse.from(savedOrder);
    }
}
