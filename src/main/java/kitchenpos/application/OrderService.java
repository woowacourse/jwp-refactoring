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
import org.springframework.util.CollectionUtils;

@Transactional(readOnly = true)
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
    public Order create(final Order requestOrder) {
        validateOrderLineItems(requestOrder.getOrderLineItems());

        final OrderTable orderTable = orderTableDao.findById(requestOrder.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        validateOrderTableNotEmpty(orderTable);

        final Order order = saveOrder(requestOrder, orderTable);
        final List<OrderLineItem> savedOrderLineItems = saveOrderLineItems(order.getId(), requestOrder);
        order.addOrderLineItems(savedOrderLineItems);

        return order;
    }

    private void validateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTableNotEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private Order saveOrder(final Order requestOrder, final OrderTable orderTable) {
        Order order = new Order(
                orderTable.getId(),
                OrderStatus.COOKING,
                LocalDateTime.now(),
                requestOrder.getOrderLineItems()
        );
        return orderDao.save(order);
    }

    private List<OrderLineItem> saveOrderLineItems(final Long orderId, final Order order) {
        final List<OrderLineItem> result = new ArrayList<>();

        for (final OrderLineItem orderLineItem : order.getOrderLineItems()) {
            orderLineItem.changeOrderId(orderId);
            result.add(orderLineItemDao.save(orderLineItem));
        }
        return result;
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.addOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order order = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        validateOrderStatus(order);

        order.changeOrderStatus(orderStatus);

        Order savedOrder = orderDao.save(order);
        savedOrder.addOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return savedOrder;
    }

    private void validateOrderStatus(final Order order) {
        if (order.isCompletion()) {
            throw new IllegalArgumentException();
        }
    }
}
