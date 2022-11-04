package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderChangeOrderStatusRequest;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderValidator orderValidator;

    public OrderService(
            final MenuDao menuDao,
            final OrderDao orderDao,
            final OrderLineItemDao orderLineItemDao,
            final OrderValidator orderValidator
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderValidator = orderValidator;
    }

    public OrderResponse create(final OrderCreateRequest request) {
        final Long orderTableId = request.getOrderTableId();
        orderValidator.validateEmpty(orderTableId);
        final Order order = new Order(orderTableId, OrderStatus.COOKING, LocalDateTime.now());
        final List<OrderLineItem> orderLineItems = createOrderLineItems(request.getOrderLineItems());
        validateOrderLineItems(orderLineItems);

        final Order savedOrder = orderDao.save(order);
        final List<OrderLineItem> savedOrderLineItems = getSavedOrderLineItems(savedOrder.getId(), orderLineItems);

        return OrderResponse.of(savedOrder, savedOrderLineItems);
    }

    private List<OrderLineItem> createOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        return orderLineItems.stream()
                .map(request -> new OrderLineItem(request.getMenuId(), request.getQuantity()))
                .collect(Collectors.toList());
    }

    private void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        validateEmpty(orderLineItems);
        validateExists(orderLineItems);
    }

    private void validateEmpty(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateExists(List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = getMenuIds(orderLineItems);
        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private List<Long> getMenuIds(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    private List<OrderLineItem> getSavedOrderLineItems(Long orderId, List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(orderLineItem -> orderLineItemDao.save(
                        new OrderLineItem(orderId, orderLineItem.getMenuId(), orderLineItem.getQuantity())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();

        return orders.stream()
                .map(order -> OrderResponse.of(order, orderLineItemDao.findAllByOrderId(order.getId())))
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeOrderStatusRequest request) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        validateOrderStatus(savedOrder);

        return OrderResponse.of(
                updateOrder(savedOrder, request.getOrderStatus()),
                orderLineItemDao.findAllByOrderId(orderId)
        );
    }

    private void validateOrderStatus(Order savedOrder) {
        if (savedOrder.isCompletion()) {
            throw new IllegalArgumentException();
        }
    }

    private Order updateOrder(Order savedOrder, OrderStatus orderStatus) {
        return orderDao.save(
                new Order(savedOrder.getId(), savedOrder.getOrderTableId(), orderStatus, savedOrder.getOrderedTime()));
    }
}
