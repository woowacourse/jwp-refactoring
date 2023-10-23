package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.springframework.stereotype.Repository;

@Repository
public class OrderCustomDao {

    private final OrderDao orderDao;

    private final OrderLineItemDao orderLineItemDao;

    public OrderCustomDao(final OrderDao orderDao, final OrderLineItemDao orderLineItemDao) {
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
    }

    public Order save(Order entity) {
        final Order savedOrder = orderDao.save(
                new Order(
                        entity.getId(),
                        entity.getOrderTableId(),
                        entity.getOrderStatus(),
                        entity.getOrderedTime(),
                        List.of()
                )
        );
        final Long orderId = savedOrder.getId();

        final List<OrderLineItem> savedOrderLineItems = cascadePersistenceForOrderLineItems(entity, orderId);

        return new Order(
                orderId,
                savedOrder.getOrderTableId(),
                savedOrder.getOrderStatus(),
                savedOrder.getOrderedTime(),
                savedOrderLineItems
        );
    }

    private List<OrderLineItem> cascadePersistenceForOrderLineItems(final Order entity, final Long orderId) {
        return entity.getOrderLineItems()
                .stream().map(orderLineItem -> orderLineItemDao.save(new OrderLineItem(
                        null,
                        orderId,
                        orderLineItem.getMenuId(),
                        orderLineItem.getQuantity()
                )))
                .collect(Collectors.toList());
    }

    public Optional<Order> findById(Long id) {
        final Optional<Order> optionalOrder = orderDao.findById(id);
        if (optionalOrder.isEmpty()) {
            return Optional.empty();
        }

        final Order order = optionalOrder.get();
        return Optional.of(new Order(
                order.getId(), order.getOrderTableId(), order.getOrderStatus(), order.getOrderedTime(),
                orderLineItemDao.findAllByOrderId(order.getId())
        ));
    }

    public List<Order> findAll() {
        final List<Order> orders = orderDao.findAll();
        return orders.stream().map(order -> new Order(
                order.getId(),
                order.getOrderTableId(),
                order.getOrderStatus(),
                order.getOrderedTime(),
                orderLineItemDao.findAllByOrderId(order.getId())
        )).collect(Collectors.toList());
    }

    public boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses) {
        return orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatuses);
    }

    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses) {
        return orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatuses);
    }

    public Order findMandatoryById(final Long id) {
        return findById(id).orElseThrow(IllegalArgumentException::new);
    }
}
