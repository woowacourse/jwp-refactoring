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
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
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
    public Order create(final OrderRequest orderRequest) {
        final Order order = toOrder(orderRequest);

        validateOrderLineItems(order);

        final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        final Order savedOrder = saveOrder(order, orderTable);

        saveOrderLineItems(savedOrder, order.getOrderLineItems());

        return savedOrder;
    }

    private Order toOrder(OrderRequest orderRequest) {
        return new Order(orderRequest.getOrderTableId(),
                orderRequest.getOrderStatus(),
                toOrderLineItems(orderRequest.getOrderLineItemRequests()));
    }

    private List<OrderLineItem> toOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        if (orderLineItemRequests == null) {
            throw new IllegalArgumentException();
        }
        return orderLineItemRequests.stream()
                .map(it -> new OrderLineItem(it.getSeq(),
                        it.getMenuId(),
                        it.getQuantity())).collect(Collectors.toList());
    }

    private void validateOrderLineItems(final Order order) {
        final List<Long> menuIds = order.getOrderMenuIds();
        order.validateOrderLineItemsSize(menuDao.countByIdIn(menuIds));
    }

    private Order saveOrder(Order order, OrderTable orderTable) {
        final Order newOrder = new Order(orderTable.getId(), OrderStatus.COOKING.name(),
                LocalDateTime.now(), order.getOrderLineItems());
        return orderDao.save(newOrder);
    }

    private void saveOrderLineItems(Order savedOrder, List<OrderLineItem> orderLineItems) {
        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.addOrderId(orderId);
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        savedOrder.changeOrderLineItems(savedOrderLineItems);
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.changeOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.validateOrderStatus();
        savedOrder.changeOrderStatus(order);
        orderDao.save(savedOrder);

        savedOrder.changeOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return savedOrder;
    }
}
