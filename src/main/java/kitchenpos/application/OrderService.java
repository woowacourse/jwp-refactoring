package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.order.OrderChangeOrderStatusRequest;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final MenuDao menuDao,
            final OrderDao orderDao,
            final OrderLineItemDao orderLineItemDao,
            final OrderTableDao orderTableDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    private static void validateOrderStatus(Order savedOrder) {
        if (savedOrder.isCompletion()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final OrderTable orderTable = findOrderTable(request.getOrderTableId());
        final Order order = Order.from(orderTable);
        final List<OrderLineItem> orderLineItems = createOrderLineItems(request.getOrderLineItems());
        validateOrderLineItems(orderLineItems);

        final Order savedOrder = orderDao.save(order);
        final List<OrderLineItem> savedOrderLineItems = getSavedOrderLineItems(savedOrder.getId(), orderLineItems);

        return OrderResponse.of(savedOrder, savedOrderLineItems);
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<OrderLineItem> createOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        return orderLineItems.stream()
                .map(request -> new OrderLineItem(request.getMenuId(), request.getQuantity()))
                .collect(Collectors.toList());
    }

    private void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        validateEmpty(orderLineItems);
        validateExists(orderLineItems);
    }

    private void validateEmpty(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateExists(List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = getMenuIds(orderLineItems);
        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private List<Long> getMenuIds(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    private List<OrderLineItem> getSavedOrderLineItems(Long orderId, List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(orderLineItem -> orderLineItemDao.save(
                        new OrderLineItem(orderId, orderLineItem.getMenuId(), orderLineItem.getQuantity())))
                .collect(Collectors.toList());
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();

        return orders.stream()
                .map(order -> OrderResponse.of(order, orderLineItemDao.findAllByOrderId(order.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeOrderStatusRequest request) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        validateOrderStatus(savedOrder);

        return OrderResponse.of(
                updateOrder(savedOrder, request.getOrderStatus()),
                orderLineItemDao.findAllByOrderId(orderId)
        );
    }

    private Order updateOrder(Order savedOrder, OrderStatus orderStatus) {
        return orderDao.save(
                new Order(savedOrder.getId(), savedOrder.getOrderTableId(), orderStatus, savedOrder.getOrderedTime()));
    }
}
