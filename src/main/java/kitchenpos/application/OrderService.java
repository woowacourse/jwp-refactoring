package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderDao orderDao,
            final OrderLineItemDao orderLineItemDao,
            final OrderTableDao orderTableDao
    ) {
        this.menuRepository = menuRepository;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public Order create(final Order order) {
        final List<OrderLineItem> orderLineItems = getOrderLineItems(order);

        final OrderTable orderTable = getOrderTable(order);

        return saveOrder(orderTable, orderLineItems);
    }

    private OrderTable getOrderTable(final Order order) {
        final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return orderTable;
    }

    private List<OrderLineItem> getOrderLineItems(final Order order) {
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        if (orderLineItems.size() != menuRepository.countByIdIn(order.getOrderLineItemsMenuIds())) {
            throw new IllegalArgumentException();
        }

        return orderLineItems;
    }

    private Order saveOrder(final OrderTable orderTable, final List<OrderLineItem> orderLineItems) {
        final Order savedOrder = orderDao.save(new Order(null, orderTable.getId()));

        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.updateOrderId(orderId);
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        savedOrder.setOrderLineItems(savedOrderLineItems);

        return savedOrder;
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrder.isOrderStatusCompletion()) {
            throw new IllegalArgumentException();
        }

        savedOrder.setOrderStatus(order.getOrderStatus());

        orderDao.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return savedOrder;
    }
}
