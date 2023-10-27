package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderLineItemRequest;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.OrderStatusChangeRequest;
import kitchenpos.order.domain.MenuExistenceValidator;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTableValidator;
import kitchenpos.order.domain.repository.OrderLineItemRepository;
import kitchenpos.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final MenuExistenceValidator menuExistenceValidator;
    private final OrderTableValidator orderTableValidator;

    public OrderService(final MenuExistenceValidator menuExistenceValidator,
                        final OrderRepository orderRepository,
                        final OrderLineItemRepository orderLineItemRepository,
                        final OrderTableValidator orderTableValidator) {
        this.menuExistenceValidator = menuExistenceValidator;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final OrderLineItems orderLineItems = createOrderLineItems(request);
        final Order savedOrder = getCurrentlySavedOrder(request.getOrderTableId(), orderLineItems);
        return OrderResponse.from(savedOrder);
    }

    private OrderLineItems createOrderLineItems(final OrderCreateRequest request) {
        final List<OrderLineItem> parsedOrderLineItems = parseToOrderLineItem(request.getOrderLineItems());
        final OrderLineItems orderLineItems = new OrderLineItems(parsedOrderLineItems);
        validateOrderLineItemsMenuAllExist(orderLineItems);
        return orderLineItems;
    }

    private List<OrderLineItem> parseToOrderLineItem(final List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(orderLineItem -> new OrderLineItem(orderLineItem.getMenuId(), orderLineItem.getQuantity()))
                .collect(Collectors.toList());
    }

    private void validateOrderLineItemsMenuAllExist(final OrderLineItems orderLineItems) {
        menuExistenceValidator.validate(orderLineItems.getMenuIds());
    }

    private Order getCurrentlySavedOrder(final Long orderTableID, final OrderLineItems orderLineItems) {
        final Order newOrder = new Order(orderTableID, LocalDateTime.now());
        newOrder.validateOrderTable(orderTableValidator);
        final Order order = orderRepository.save(newOrder);
        final OrderLineItems savedOrderLineItems = saveOrderLineItems(order, orderLineItems);
        order.registerOrderLineItems(savedOrderLineItems);
        return order;
    }

    private OrderLineItems saveOrderLineItems(final Order order, final OrderLineItems orderLineItems) {
        final List<OrderLineItem> savedOrderLineItems = orderLineItems.getItems()
                .stream()
                .map(orderLineItem -> {
                    orderLineItem.orderedBy(order.getId());
                    return orderLineItemRepository.save(orderLineItem);
                }).collect(Collectors.toList());
        return new OrderLineItems(savedOrderLineItems);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        for (final Order order : orders) {
            final List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(order.getId());
            order.registerOrderLineItems(new OrderLineItems(orderLineItems));
        }

        return OrderResponse.listOf(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order order = orderRepository.save(getStatusChangedOrder(orderId, request.getOrderStatus()));
        final List<OrderLineItem> savedOrderItems = orderLineItemRepository.findAllByOrderId(order.getId());
        order.registerOrderLineItems(new OrderLineItems(savedOrderItems));
        return OrderResponse.from(order);
    }

    private Order getStatusChangedOrder(final Long orderId, final String orderStatus) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        order.changeStatus(OrderStatus.valueOf(orderStatus));
        return order;
    }
}
