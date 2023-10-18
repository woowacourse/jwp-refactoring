package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderCreateRequest.OrderLineItemInfo;
import kitchenpos.order.application.dto.OrderStatusChangeRequest;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderValidator orderValidator;

    public OrderService(
            OrderDao orderDao,
            OrderLineItemDao orderLineItemDao,
            OrderValidator orderValidator
    ) {
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public Order create(final OrderCreateRequest request) {
        List<OrderLineItem> orderLineItems = orderLineItems(request.getOrderLineItems());
        final Order savedOrder = orderDao.save(
                new Order(request.getOrderTableId(), orderLineItems, orderValidator)
        );
        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrderId(orderId);
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        savedOrder.setOrderLineItems(savedOrderLineItems);
        return savedOrder;
    }

    private List<OrderLineItem> orderLineItems(List<OrderLineItemInfo> orderLineItems) {
        return orderLineItems.stream()
                .map(it -> new OrderLineItem(it.getMenuId(), it.getQuantity()))
                .toList();
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());
        orderDao.save(savedOrder);
        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));
        return savedOrder;
    }
}
