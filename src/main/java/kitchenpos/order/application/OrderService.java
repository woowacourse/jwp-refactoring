package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.request.OrderCreateRequest;
import kitchenpos.ordertable.dao.OrderTableDao;
import kitchenpos.ordertable.domain.OrderTable;
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
    public Order create(OrderCreateRequest request) {
        List<OrderLineItem> orderLineItems = request.getOrderLineItems().stream()
                .map(orderLineItem -> new OrderLineItem(orderLineItem.getMenuId(), orderLineItem.getQuantity()))
                .collect(Collectors.toList());
        validateOrderLineItemsSize(orderLineItems);

        OrderTable orderTable = orderTableDao.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        Order savedOrder = orderDao.save(Order.create(orderTable, orderLineItems));

        savedOrder.assignOrderToTable();
        for (OrderLineItem orderLineItem : savedOrder.getOrderLineItems()) {
            orderLineItemDao.save(orderLineItem);
        }

        return savedOrder;
    }

    private void validateOrderLineItemsSize(List<OrderLineItem> orderLineItems) {
        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
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
    public Order changeOrderStatus(Long orderId, OrderStatus orderStatus) {
        Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(orderStatus);
        orderDao.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));
        return savedOrder;
    }
}
