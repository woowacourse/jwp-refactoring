package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import kitchenpos.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public Order create(OrderCreateRequest orderCreateRequest) {
        OrderLineItems orderLineItems = generateOrderLineItems(orderCreateRequest);
        OrderTable orderTable = generateOrderTable(orderCreateRequest);

        Order savedOrder = orderDao.save(generateOrder(orderTable));

        final Long orderId = savedOrder.getId();
        List<OrderLineItem> lastOrderLineItems = orderLineItems.changeOrderId(orderId).getOrderLineItems();
        lastOrderLineItems.forEach(orderLineItemDao::save);
        return savedOrder.changeOrderLineItems(lastOrderLineItems);
    }

    private OrderLineItems generateOrderLineItems(OrderCreateRequest orderCreateRequest) {
        validateExistMenu(orderCreateRequest);
        Map<Long, Long> groupByMenuId = orderCreateRequest.getOrderLineItems().stream()
                .collect(Collectors.toMap(OrderLineItemRequest::getMenuId, OrderLineItemRequest::getQuantity));

        List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        groupByMenuId.keySet()
                .forEach(each -> savedOrderLineItems.add(new OrderLineItem(each, groupByMenuId.get(each))));

        return new OrderLineItems(savedOrderLineItems);
    }

    private void validateExistMenu(OrderCreateRequest orderCreateRequest) {
        List<Long> existMenuIds = menuDao.findAll().stream()
                .map(Menu::getId)
                .collect(Collectors.toUnmodifiableList());
        if (orderCreateRequest.getOrderLineItems().stream()
                .anyMatch(each -> !existMenuIds.contains(each.getMenuId()))) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable generateOrderTable(OrderCreateRequest orderCreateRequest) {
        OrderTable orderTable = orderTableDao.findById(orderCreateRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        validateNonEmptyOrderTable(orderTable);
        return orderTable;
    }

    private void validateNonEmptyOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private Order generateOrder(OrderTable orderTable) {
        return new Order(orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now());
    }

    public List<Order> list() {
        return orderDao.findAll().stream()
                .map(each -> each.changeOrderLineItems(orderLineItemDao.findAllByOrderId(each.getId())))
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, OrderStatusUpdateRequest orderStatusUpdateRequest) {
        Order searchedOrder = searchOrder(orderId);
        final OrderStatus orderStatus = OrderStatus.valueOf(orderStatusUpdateRequest.getOrderStatus());

        searchedOrder = orderDao.save(searchedOrder.changeOrderStatus(orderStatus));

        return searchedOrder.changeOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));
    }

    private Order searchOrder(Long orderId) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        validateCompletedOrder(savedOrder);
        return savedOrder;
    }

    private void validateCompletedOrder(Order order) {
        if (order.isCompletion()) {
            throw new IllegalArgumentException();
        }
    }
}
