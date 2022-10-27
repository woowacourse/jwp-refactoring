package kitchenpos.application;

import java.util.List;
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
        validateOrderLineItems(order);

        final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        validateIfTableEmpty(orderTable);

        final Order savedOrder = orderDao.save(
                new Order(
                        orderTable.getId(),
                        OrderStatus.COOKING.name(),
                        order.getOrderedTime(),
                        order.getOrderLineItems()
                )
        );
        updateOrderLineItemsByOrderId(order, savedOrder.getId());

        return savedOrder;
    }

    private void validateOrderLineItems(final Order order) {
        if (order.isOrderLineItemsEmpty()) {
            throw new IllegalArgumentException();
        }

        final long orderMenuIdCounts = menuDao.countByIdIn(order.getOrderMenuIds());
        if (!order.hasValidMenus(orderMenuIdCounts)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateIfTableEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void updateOrderLineItemsByOrderId(final Order order, final Long savedOrderId) {
        order.addOrderIdsToOrderLineItems(savedOrderId);
        for (final OrderLineItem orderLineItem : order.getOrderLineItemsList()) {
            orderLineItemDao.update(orderLineItem);
        }
    }

    public List<Order> list() {
        return orderDao.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final String orderStatus) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        validateChangableStatus(savedOrder);
        savedOrder.changeOrderStatus(orderStatus);

        orderDao.update(savedOrder);
        return savedOrder;
    }

    private void validateChangableStatus(final Order savedOrder) {
        if (savedOrder.isCompletionOrder()) {
            throw new IllegalArgumentException();
        }
    }
}
