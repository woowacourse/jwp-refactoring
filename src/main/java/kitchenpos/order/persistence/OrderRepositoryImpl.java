package kitchenpos.order.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.order.application.entity.OrderEntity;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

  private final OrderDao orderDao;
  private final OrderLineItemDao orderLineItemDao;

  public OrderRepositoryImpl(final OrderDao orderDao, final OrderLineItemDao orderLineItemDao) {
    this.orderDao = orderDao;
    this.orderLineItemDao = orderLineItemDao;
  }

  @Override
  public Order save(final Order entity) {
    final Order savedOrder = orderDao.save(OrderEntity.from(entity)).toOrder();

    final List<OrderLineItem> savedOrderLineItems = saveOrderLineItems(entity, savedOrder);
    return new Order(savedOrder.getId(), savedOrder.getOrderTableId(), savedOrder.getOrderStatus(),
        savedOrder.getOrderedTime(), savedOrderLineItems);
  }

  private List<OrderLineItem> saveOrderLineItems(final Order entity, final Order savedOrder) {
    final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
    for (final OrderLineItem orderLineItem : entity.getOrderLineItems()) {
      savedOrderLineItems.add(orderLineItemDao.save(
          new OrderLineItem(savedOrder.getId(), orderLineItem.getMenuId(),
              orderLineItem.getQuantity())));
    }
    return savedOrderLineItems;
  }

  @Override
  public Optional<Order> findById(final Long id) {
    return orderDao.findById(id).map(OrderEntity::toOrder);
  }

  @Override
  public List<Order> findAll() {
    final List<Order> orders = orderDao.findAll()
        .stream()
        .map(OrderEntity::toOrder)
        .collect(Collectors.toList());

    final List<Order> result = new ArrayList<>();
    for (final Order order : orders) {
      result.add(new Order(order.getId(), order.getOrderTableId(), order.getOrderStatus(),
          order.getOrderedTime(), orderLineItemDao.findAllByOrderId(order.getId())));
    }

    return result;
  }

  @Override
  public boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId,
      final List<String> orderStatuses) {
    return orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatuses);
  }

  @Override
  public boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds,
      final List<String> orderStatuses) {
    return orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatuses);
  }
}
