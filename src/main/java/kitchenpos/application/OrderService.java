package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public Order create(final Order order) {
        order.validateNotEmptyOrderLineItems();
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        validateOrderMenu(order, orderLineItems);

        final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        orderTable.validateOrderable();

        final Order newOrder = new Order(null, orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now());
        final Order savedOrder = orderDao.save(newOrder);

        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrderId(orderId);
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        return new Order(
                savedOrder.getId(),
                savedOrder.getOrderTableId(),
                savedOrder.getOrderStatus(),
                savedOrder.getOrderedTime(),
                savedOrderLineItems);
    }

    private void validateOrderMenu(final Order order, final List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        order.validateExistMenu(menuDao.countByIdIn(menuIds));
    }

    public List<Order> list() {
        final List<Order> orders = new ArrayList<>();
        for (final Order order : orderDao.findAll()) {
            orders.add(new Order(order.getId(), order.getOrderTableId(), order.getOrderStatus(), order.getOrderedTime(),
                    orderLineItemDao.findAllByOrderId(order.getId())));
        }
        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        order.validateOrderNotCompletion();

        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        final Order newOrder = new Order(
                savedOrder.getId(),
                savedOrder.getOrderTableId(),
                orderStatus.name(),
                savedOrder.getOrderedTime());

        orderDao.save(newOrder);

        return new Order(
                savedOrder.getId(),
                savedOrder.getOrderTableId(),
                orderStatus.name(),
                savedOrder.getOrderedTime(),
                orderLineItemDao.findAllByOrderId(orderId));
    }
}
