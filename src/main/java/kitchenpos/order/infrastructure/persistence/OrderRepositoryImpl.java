package kitchenpos.order.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.order_table.infrastructure.persistence.OrderTableDao;
import kitchenpos.order_table.infrastructure.persistence.OrderTableMapper;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

  private final OrderDao orderDao;
  private final OrderTableDao orderTableDao;
  private final OrderLineItemDao orderLineItemDao;

  public OrderRepositoryImpl(
      final OrderDao orderDao,
      final OrderTableDao orderTableDao,
      final OrderLineItemDao orderLineItemDao
  ) {
    this.orderDao = orderDao;
    this.orderTableDao = orderTableDao;
    this.orderLineItemDao = orderLineItemDao;
  }

  @Override
  public Order save(final Order order) {
    final OrderEntity entity = new OrderEntity(
        order.getOrderTable().getId(),
        order.getOrderStatus().name(),
        order.getOrderedTime()
    );

    final OrderEntity savedOrderEntity = orderDao.save(entity);

    final List<OrderLineItem> savedOrderLineItems = savedOrderLineItems(order, savedOrderEntity);

    return OrderMapper.mapToOrder(
        savedOrderEntity,
        order.getOrderTable(),
        savedOrderLineItems
    );
  }

  private List<OrderLineItem> savedOrderLineItems(
      final Order order,
      final OrderEntity savedOrderEntity
  ) {
    final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();

    for (final OrderLineItem orderLineItem : order.getOrderLineItems()) {
      savedOrderLineItems.add(saveOrderLineItem(orderLineItem, savedOrderEntity));
    }

    return savedOrderLineItems;
  }

  private OrderLineItem saveOrderLineItem(
      final OrderLineItem orderLineItem,
      final OrderEntity orderEntity
  ) {
    return OrderMapper.mapToOrderLineItem(
        orderLineItemDao.save(
            new OrderLineItemEntity(
                orderEntity.getId(),
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity()
            )
        )
    );
  }

  @Override
  public Optional<Order> findById(final Long id) {
    return orderDao.findById(id)
        .map(entity -> OrderMapper.mapToOrder(
            entity,
            findOrderTable(entity),
            findOrderLineItem(entity)));
  }

  private OrderTable findOrderTable(final OrderEntity savedOrderEntity) {
    return orderTableDao.findById(savedOrderEntity.getOrderTableId())
        .map(OrderTableMapper::mapToOrderTable)
        .orElseThrow(IllegalArgumentException::new);
  }

  private List<OrderLineItem> findOrderLineItem(final OrderEntity savedOrderEntity) {
    return orderLineItemDao.findAllByOrderId(savedOrderEntity.getId())
        .stream()
        .map(OrderMapper::mapToOrderLineItem)
        .collect(Collectors.toList());
  }

  @Override
  public List<Order> findAll() {
    return orderDao.findAll()
        .stream()
        .map(entity -> OrderMapper.mapToOrder(
            entity,
            findOrderTable(entity),
            findOrderLineItem(entity)))
        .collect(Collectors.toList());
  }

  @Override
  public boolean existsByOrderTableIdAndOrderStatusIn(
      final Long orderTableId,
      final List<String> orderStatuses
  ) {
    return orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatuses);
  }

  @Override
  public boolean existsByOrderTableIdInAndOrderStatusIn(
      final List<Long> orderTableIds,
      final List<String> orderStatuses
  ) {
    return orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatuses);
  }
}
