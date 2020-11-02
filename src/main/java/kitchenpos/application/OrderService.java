package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

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
    public Order create(final Order order) {
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());

        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        Order newOrder = order.toBuilder()
            .id(null)
            .build();

        final OrderTable orderTable = orderTableDao.findById(newOrder.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        newOrder = newOrder.toBuilder()
            .orderTableId(orderTable.getId())
            .orderStatus(OrderStatus.COOKING.name())
            .orderedTime(LocalDateTime.now())
            .build();

        final Order savedOrder = orderDao.save(newOrder);

        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            OrderLineItem newOrderLineItem = orderLineItem.toBuilder()
                .orderId(orderId)
                .build();
            savedOrderLineItems.add(orderLineItemDao.save(newOrderLineItem));
        }

        return savedOrder.toBuilder()
            .orderLineItems(savedOrderLineItems)
            .build();
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();
        List<Order> result = new ArrayList<>();

        for (final Order order : orders) {
            Order newOrder = order.toBuilder()
                .orderLineItems(orderLineItemDao.findAllByOrderId(order.getId()))
                .build();
            result.add(newOrder);
        }

        return result;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderDao.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        Order newOrder = savedOrder.toBuilder()
            .orderStatus(orderStatus.name())
            .build();

        orderDao.save(newOrder);

        return newOrder.toBuilder()
            .orderLineItems(orderLineItemDao.findAllByOrderId(orderId))
            .build();
    }
}
