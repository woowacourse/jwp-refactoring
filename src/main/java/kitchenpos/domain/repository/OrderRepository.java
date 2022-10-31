package kitchenpos.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.springframework.stereotype.Component;

@Component
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;

    public OrderRepository(OrderDao orderDao, OrderLineItemDao orderLineItemDao) {
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
    }


    public Order save(Order order) {
        Order orderWithLocalDateTime = new Order(order.getOrderTableId(), LocalDateTime.now(),
                order.getOrderLineItems());
        Long orderId = orderDao.save(orderWithLocalDateTime).getId();

        List<OrderLineItem> savedOrderLineItems = saveOrderLineItems(order, orderId);

        return new Order(orderId, order.getOrderTableId(), order.getOrderStatus(), order.getOrderedTime(),
                savedOrderLineItems);
    }

    private List<OrderLineItem> saveOrderLineItems(Order order, Long orderId) {
        return order.getOrderLineItems().stream()
                .map(orderLineItem -> new OrderLineItem(
                        orderId, orderLineItem.getMenuId(), orderLineItem.getQuantity()))
                .map(orderLineItemDao::save)
                .collect(Collectors.toList());
    }

    public List<Order> findAll() {
        return orderDao.findAll()
                .stream()
                .map(order -> new Order(
                        order.getId(),
                        order.getOrderTableId(),
                        order.getOrderStatus(),
                        order.getOrderedTime(),
                        orderLineItemDao.findAllByOrderId(order.getId())
                )).collect(Collectors.toList());
    }

    public Order findById(Long id) {
        Order savedOrder = orderDao.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        return new Order(savedOrder.getId(), savedOrder.getOrderTableId(), savedOrder.getOrderStatus(),
                savedOrder.getOrderedTime(), orderLineItemDao.findAllByOrderId(id));
    }

    public Order update(Order order) {
        return orderDao.save(order);
    }

    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatus) {
        return orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatus);
    }

    public boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatus) {
        return orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatus);
    }
}
