package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderLineItemRequest;
import kitchenpos.order.application.dto.OrderStatusChangeRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.repository.OrderLineItemRepository;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final OrderCreateRequest request) {
        final OrderLineItems orderLineItems = createOrderLineItems(request);
        final OrderTable orderTable = findSavedOrderTableById(request.getOrderTableId());
        validateOrderTableNotEmpty(orderTable);
        return getCurrentlySavedOrder(orderTable, orderLineItems);
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
        if (orderLineItems.getItemSize() != menuRepository.countByIdIn(orderLineItems.getMenuIds())) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable findSavedOrderTableById(final Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateOrderTableNotEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private Order getCurrentlySavedOrder(final OrderTable orderTable, final OrderLineItems orderLineItems) {
        final Order order = orderRepository.save(new Order(orderTable.getId(), LocalDateTime.now()));
        orderLineItems.orderedBy(order);
        final OrderLineItems savedOrderLineItems = saveOrderLineItems(orderLineItems);
        order.registerOrderLineItems(savedOrderLineItems);
        return order;
    }

    private OrderLineItems saveOrderLineItems(final OrderLineItems orderLineItems) {
        final List<OrderLineItem> savedOrderLineItems = orderLineItems.getItems()
                .stream()
                .map(orderLineItemRepository::save)
                .collect(Collectors.toList());
        return new OrderLineItems(savedOrderLineItems);
    }

    public List<Order> list() {
        final List<Order> orders = orderRepository.findAll();

        for (final Order order : orders) {
            final List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(order.getId());
            order.registerOrderLineItems(new OrderLineItems(orderLineItems));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order order = orderRepository.save(getStatusChangedOrder(orderId, request.getOrderStatus()));
        final List<OrderLineItem> savedOrderItems = orderLineItemRepository.findAllByOrderId(order.getId());
        order.registerOrderLineItems(new OrderLineItems(savedOrderItems));
        return order;
    }

    private Order getStatusChangedOrder(final Long orderId, final String orderStatus) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        order.changeStatus(OrderStatus.valueOf(orderStatus));
        return order;
    }
}
