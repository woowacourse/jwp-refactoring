package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderValidator;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderStatusChangeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderValidator orderValidator;

    public OrderService(
            final OrderDao orderDao,
            final OrderLineItemDao orderLineItemDao,
            final OrderValidator orderValidator) {
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        final Order order = new Order(orderRequest.getOrderTableId(), orderRequest.getOrderLineItems());
        orderValidator.validate(order);
        final Order savedOrder = orderDao.save(order);
        savedOrder.setOrderLineItems(saveOrderLineItems(savedOrder.getId(), orderRequest.getOrderLineItems()));
        return savedOrder;
    }

    private List<OrderLineItem> saveOrderLineItems(final Long orderId, final List<OrderLineItem> orderLineItems) {
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrderId(orderId);
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        return savedOrderLineItems;
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
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.changeStatus(orderStatus);

        orderDao.save(savedOrder);
        final List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(savedOrder.getId());
        savedOrder.setOrderLineItems(orderLineItems);
        return savedOrder;
    }
}
