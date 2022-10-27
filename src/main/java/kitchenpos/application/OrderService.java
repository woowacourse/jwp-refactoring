package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
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

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        validateMenuExists(orderLineItems, menuIds);
        final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        validateIsEmpty(orderTable);

        final Order savedOrder = orderDao.save(Order.create(orderTable.getId()));
        setOrderItemsInOrder(orderLineItems, savedOrder, savedOrder.getId());

        return savedOrder;
    }

    private void validateIsEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void setOrderItemsInOrder(List<OrderLineItem> orderLineItems, Order savedOrder, Long orderId) {
        ArrayList<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrderId(orderId);
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        savedOrder.addOrderLineItems(savedOrderLineItems);
    }

    private void validateMenuExists(List<OrderLineItem> orderLineItems, List<Long> menuIds) {
        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    public List<Order> list() {
        return orderDao.findAll().stream()
                .peek(order -> order.addOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        validateCompletionStatus(savedOrder);

        savedOrder.setOrderStatus(order.getOrderStatus());

        orderDao.save(savedOrder);

        savedOrder.addOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return savedOrder;
    }

    private void validateCompletionStatus(Order savedOrder) {
        if (savedOrder.isCompletionStatus()) {
            throw new IllegalArgumentException("[ERROR] 주문이 완료된 상태에서는 상태를 못 바꿉니다.");
        }
    }
}
