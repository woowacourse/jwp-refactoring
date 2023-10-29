package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.application.dto.request.OrderCreateRequest;
import kitchenpos.order.application.dto.request.OrderStatusChangeRequest;
import kitchenpos.order.application.dto.response.OrderResponse;
import kitchenpos.order.application.dto.request.OrderLineItemRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.service.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
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

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        orderValidator.validatePresentTable(request.getOrderTableId());
        final Order order = new Order(
                request.getOrderTableId(),
                LocalDateTime.now()
        );
        order.updateOrderLineItems(extractOrderLineItems(request.getOrderLineItems()));
        return OrderResponse.from(orderRepository.save(order));
    }

    private List<OrderLineItem> extractOrderLineItems(final List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(it -> new OrderLineItem(
                        OrderMenu.from(menuRepository.getByIdOrThrow(it.getMenuId())), it.getQuantity())
                )
                .collect(Collectors.toList());
    }

    public List<OrderResponse> list() {
        return OrderResponse.from(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order order = orderRepository.getByIdOrThrow(orderId);
        order.updateOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));
        return OrderResponse.from(orderRepository.save(order));
    }
}
