package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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

    @Transactional
    public Order create(Order order) {
        List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Order newOrder = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        Order savedOrder = orderDao.save(newOrder);

        Long orderId = savedOrder.getId();
        List<OrderLineItem> savedOrderLineItems = orderLineItems.stream()
                .map(orderLineItem -> orderLineItemDao.save(new OrderLineItem(
                        orderId,
                        orderLineItem.getMenuId(),
                        orderLineItem.getQuantity()
                ))).collect(Collectors.toList());

        return new Order(orderId, savedOrder.getOrderTableId(), savedOrder.getOrderStatus(),
                savedOrder.getOrderedTime(), savedOrderLineItems);
    }

    public List<Order> list() {
        List<Order> orders = orderDao.findAll();

        return orders.stream()
                .map(order -> new Order(
                        order.getId(),
                        order.getOrderTableId(),
                        order.getOrderStatus(),
                        order.getOrderedTime(),
                        orderLineItemDao.findAllByOrderId(order.getId())
                )).collect(Collectors.toList());
    }

    @Transactional
    public Order changeOrderStatus(Long orderId, Order order) {
        Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());

        Order updateOrder = new Order(savedOrder.getId(), savedOrder.getOrderTableId(), orderStatus.name(),
                savedOrder.getOrderedTime(), orderLineItemDao.findAllByOrderId(orderId));

        return orderDao.save(updateOrder);
    }
}
