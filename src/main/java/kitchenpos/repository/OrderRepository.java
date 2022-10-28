package kitchenpos.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;

    public OrderRepository(OrderDao orderDao, OrderLineItemDao orderLineItemDao) {
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
    }

    public Order save(Order order) {
        Order savedOrder = orderDao.save(order);

        Long orderId = savedOrder.getId();
        for (OrderLineItem orderLineItem : order.getOrderLineItems()) {
            OrderLineItem toSaveProduct = new OrderLineItem(orderId, orderLineItem.getMenuId(),
                    orderLineItem.getQuantity());
            savedOrder.addOrderLineItem(orderLineItemDao.save(toSaveProduct));
        }

        return savedOrder;
    }

    public List<Order> findAll() {
        List<Order> orders = orderDao.findAll();

        for (Order order : orders) {
            order.addOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }
        return orders;
    }

    public void update(Order order) {
        orderDao.save(order);
    }
}
