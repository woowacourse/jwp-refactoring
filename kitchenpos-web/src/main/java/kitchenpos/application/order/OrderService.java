package kitchenpos.application.order;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.order.dto.OrderChangeStatusRequest;
import kitchenpos.application.order.dto.OrderCreateRequest;
import kitchenpos.application.order.dto.OrderLineItemRequest;
import kitchenpos.application.order.dto.OrderResponse;
import kitchenpos.application.order.dto.OrdersResponse;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menu.exception.MenuException.NotFoundMenuException;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderValidator;
import kitchenpos.domain.order.exception.OrderException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(final MenuRepository menuRepository, final OrderRepository orderRepository,
                        final OrderValidator orderValidator) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final List<OrderLineItemRequest> orderLineItemRequests = request.getOrderLineItems();
        final List<Long> menuIds = orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        final Long orderTableId = request.getOrderTableId();
        orderValidator.validateCreate(orderTableId);

        List<OrderLineItem> orderLineItems = getOrderLineItems(request);
        final Order order = Order.from(orderTableId, orderLineItemRequests.size(), menuRepository.countByIdIn(menuIds), orderLineItems);
        final Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);
    }

    private List<OrderLineItem> getOrderLineItems(final OrderCreateRequest request) {
        return request.getOrderLineItems().stream()
                .map(orderLineItemRequest -> {
                    final Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                            .orElseThrow(NotFoundMenuException::new);
                    return new OrderLineItem(menu.getName(), menu.getPrice(), orderLineItemRequest.getQuantity());
                }).collect(Collectors.toUnmodifiableList());
    }

    public OrdersResponse list() {
        return OrdersResponse.from(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(OrderException.NotFoundOrderException::new);

        savedOrder.changeStatus(OrderStatus.valueOf(request.getOrderStatus()));
        return OrderResponse.from(savedOrder);
    }
}
