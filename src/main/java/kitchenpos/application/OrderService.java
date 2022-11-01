package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.application.dto.OrderUpdateRequest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;
    private final OrderValidator orderValidator;

    public OrderService(OrderDao orderDao, OrderLineItemDao orderLineItemDao, OrderTableDao orderTableDao,
                        OrderValidator orderValidator) {
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public Order create(final OrderCreateRequest request) {
        Order order = Order.create(request.getOrderTableId(),
                request.getOrderLineItems(),
                orderValidator);

        return orderDao.save(order);
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();
        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderUpdateRequest request) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.checkUpdatable();

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus);

        orderDao.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return savedOrder;
    }
}
