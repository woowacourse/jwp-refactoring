package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.order.OrderCreateRequest;
import kitchenpos.ui.dto.order.OrderLineItemRequest;
import kitchenpos.ui.dto.order.OrderStatusChangeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (orderLineItems.getItemSize() != menuDao.countByIdIn(orderLineItems.getMenuIds())) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable findSavedOrderTableById(final Long id) {
        return orderTableDao.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateOrderTableNotEmpty(final OrderTable orderTable) {
        if (orderTable.getEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private Order getCurrentlySavedOrder(final OrderTable orderTable, final OrderLineItems orderLineItems) {
        final Order order = orderDao.save(new Order(orderTable.getId(), LocalDateTime.now()));
        orderLineItems.orderedBy(order);
        final OrderLineItems savedOrderLineItems = saveOrderLineItems(orderLineItems);
        order.registerOrderLineItems(savedOrderLineItems);
        return order;
    }

    private OrderLineItems saveOrderLineItems(final OrderLineItems orderLineItems) {
        final List<OrderLineItem> savedOrderLineItems = orderLineItems.getItems()
                .stream()
                .map(orderLineItemDao::save)
                .collect(Collectors.toList());
        return new OrderLineItems(savedOrderLineItems);
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            final List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(order.getId());
            order.registerOrderLineItems(new OrderLineItems(orderLineItems));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order order = orderDao.save(getStatusChangedOrder(orderId, request.getOrderStatus()));
        final List<OrderLineItem> savedOrderItems = orderLineItemDao.findAllByOrderId(order.getId());
        order.registerOrderLineItems(new OrderLineItems(savedOrderItems));
        return order;
    }

    private Order getStatusChangedOrder(final Long orderId, final String orderStatus) {
        final Order order = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        order.changeStatus(OrderStatus.valueOf(orderStatus));
        return order;
    }
}
