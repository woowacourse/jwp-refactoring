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
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemResponse;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderUpdateRequest;
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
    public OrderResponse create(final OrderCreateRequest request) {
        final Order order = request.toOrder();
        checkExistMenuIn(order);
        final OrderTable orderTable = getOrderTable(order);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        setOrderStatusAndTime(order);
        final Order savedOrder = orderDao.save(order);
        final List<OrderLineItem> savedOrderLineItems = getOrderLineItems(order, savedOrder);
        savedOrder.setOrderLineItems(savedOrderLineItems);

        return OrderResponse.from(savedOrder);
    }

    private void checkExistMenuIn(Order order) {
        final long menuCount = menuDao.countByIdIn(order.getMenuIds());
        if (!order.hasValidSize(menuCount)) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable getOrderTable(Order order) {
        return orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
    }

    private void setOrderStatusAndTime(Order order) {
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
    }

    private List<OrderLineItem> getOrderLineItems(Order order, Order savedOrder) {
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : order.getOrderLineItems()) {
            orderLineItem.setOrderId(savedOrder.getId());
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        return savedOrderLineItems;
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();
        return orders.stream()
                .map(order -> new OrderResponse(
                                order.getId(),
                                order.getOrderTableId(),
                                order.getOrderStatus(),
                                order.getOrderedTime(),
                                OrderLineItemResponse.from(orderLineItemDao.findAllByOrderId(order.getId()))
                        )
                )
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderUpdateRequest request) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrder.hasStatus(OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.from(request.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());

        orderDao.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return OrderResponse.from(savedOrder);
    }
}
