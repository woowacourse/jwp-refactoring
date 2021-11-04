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
import kitchenpos.dto.OrderRequest;
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
    public Order create(final OrderRequest orderRequest) {
        checkValidOrder(orderRequest);

        final OrderTable orderTable = findOrderTableById(orderRequest.getOrderTableId());
        Order order = new Order(orderTable.getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                orderRequest.getOrderLineItems());
        return saveOrder(order);
    }

    private Order saveOrder(Order order) {
        final Order savedOrder = orderDao.save(order);

        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        order.setOrderIdsInLineItems(order.getId());
        for (final OrderLineItem orderLineItem : order.getOrderLineItems()) {
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        return orderDao.findById(savedOrder.getId()).orElseThrow(IllegalArgumentException::new);
    }

    private OrderTable findOrderTableById(Long id) {
        final OrderTable orderTable = orderTableDao.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return orderTable;
    }

    private void checkValidOrder(OrderRequest orderRequest) {
        final List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItems();
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
        if (isNotAllSavedMenuIds(orderRequest)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isNotAllSavedMenuIds(OrderRequest orderRequest) {
        final List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItems();
        final List<Long> menuIds = orderLineItems.stream().map(OrderLineItem::getMenuId).collect(Collectors.toList());
        return orderLineItems.size() != menuDao.countByIdIn(menuIds);
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
        final Order savedOrder = findById(orderId);
        checkValidOrderForChangeStatus(savedOrder);

        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());
        orderDao.save(savedOrder);
        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));
        return savedOrder;
    }

    private void checkValidOrderForChangeStatus(Order savedOrder) {
        if (isCompletionStatus(savedOrder)) {
            throw new IllegalArgumentException();
        }
    }

    private Order findById(Long orderId) {
        return orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private boolean isCompletionStatus(Order savedOrder) {
        return Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus());
    }
}
