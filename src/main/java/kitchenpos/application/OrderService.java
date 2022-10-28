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

        final OrderTable orderTable = orderTableDao.findById(orderCreateRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final Order savedOrder = orderDao.save(new Order(orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now()));


        final Long orderId = savedOrder.getId();
        List<OrderLineItem> lastOrderLineItems = orderLineItems.changeOrderId(orderId).getOrderLineItems();
        lastOrderLineItems.forEach(orderLineItemDao::save);
        savedOrder.setOrderLineItems(lastOrderLineItems);

        return savedOrder;
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

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, OrderStatusUpdateRequest orderStatusUpdateRequest) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(orderStatusUpdateRequest.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());

        orderDao.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return savedOrder;
    }
}
