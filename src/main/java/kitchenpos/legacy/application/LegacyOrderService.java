package kitchenpos.legacy.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderStatus;
import kitchenpos.legacy.dao.MenuDao;
import kitchenpos.legacy.dao.OrderDao;
import kitchenpos.legacy.dao.OrderLineItemDao;
import kitchenpos.legacy.dao.OrderTableDao;
import kitchenpos.legacy.domain.Order;
import kitchenpos.legacy.domain.OrderLineItem;
import kitchenpos.legacy.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class LegacyOrderService {

    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public LegacyOrderService(
        MenuDao menuDao,
        OrderDao orderDao,
        OrderLineItemDao orderLineItemDao,
        OrderTableDao orderTableDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

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

        order.setId(null);

        OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        order.setOrderTableId(orderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());

        Order savedOrder = orderDao.save(order);

        Long orderId = savedOrder.getId();
        List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrderId(orderId);
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        savedOrder.setOrderLineItems(savedOrderLineItems);

        return savedOrder;
    }

    @Transactional(readOnly = true)
    public List<Order> findAll() {
        List<Order> orders = orderDao.findAll();

        for (Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    public Order changeOrderStatus(Long orderId, Order order) {
        Order savedOrder = orderDao.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());

        orderDao.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return savedOrder;
    }
}
