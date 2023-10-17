package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.application.dto.OrderCreateRequest.OrderLineItemInfo;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderValidator;
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
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());

        orderDao.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return savedOrder;
    }
}
