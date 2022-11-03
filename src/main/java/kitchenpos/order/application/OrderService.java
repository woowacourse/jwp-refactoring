package kitchenpos.order.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.application.request.OrderRequest;
import kitchenpos.order.application.response.OrderResponse;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderValidator;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;

    private final OrderValidator orderValidator;

    public OrderService(final OrderDao orderDao,
        final OrderLineItemDao orderLineItemDao,
        final OrderValidator orderValidator) {
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        List<OrderLineItem> orderLineItems = request.toOrderLineItems();
        Order order = new Order(request.getOrderTableId(), orderLineItems);
        orderValidator.validate(order);
        Order savedOrder = orderDao.save(order);

        saveOrderLineItems(orderLineItems, savedOrder);
        return OrderResponse.from(savedOrder);
    }

    private void saveOrderLineItems(final List<OrderLineItem> orderLineItems, final Order order) {
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItemDao.save(
                new OrderLineItem(order.getId(), orderLineItem.getOrderMenuId(), orderLineItem.getQuantity())
            );
        }
    }

    public List<OrderResponse> list() {
        return OrderResponse.fromAll(orderDao.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
        final Order savedOrder = orderDao.getById(orderId);
        savedOrder.changeStatus(request.getOrderStatus());

        return OrderResponse.from(savedOrder);
    }
}
