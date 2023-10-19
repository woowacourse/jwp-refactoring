package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.entity.OrderEntity;
import kitchenpos.dao.entity.OrderLineItemEntity;
import kitchenpos.dao.entity.OrderTableEntity;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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

    final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();

    for (final OrderLineItem orderLineItem : order.getOrderLineItems()) {
      final OrderLineItemEntity savedEntity = orderLineItemDao.save(
          new OrderLineItemEntity(
              savedOrderEntity.getId(),
              orderLineItem.getMenuId(),
              orderLineItem.getQuantity()
          )
      );

      savedOrderLineItems.add(
          new OrderLineItem(
              savedEntity.getSeq(),
              orderLineItem.getMenuId(),
              savedEntity.getQuantity()
          )
      );
    }

    return new Order(
        savedOrderEntity.getId(),
        order.getOrderTable(),
        order.getOrderStatus(),
        order.getOrderedTime(),
        savedOrderLineItems
    );
  }

  private Order mapToOrder(final OrderEntity savedOrderEntity) {
    return new Order(
        savedOrderEntity.getId(),
        findOrderTable(savedOrderEntity),
        OrderStatus.valueOf(savedOrderEntity.getOrderStatus()),
        savedOrderEntity.getOrderedTime(),
        findOrderLineItem(savedOrderEntity)
    );
  }

  private OrderTable findOrderTable(final OrderEntity savedOrderEntity) {
    return orderTableDao.findById(savedOrderEntity.getOrderTableId())
        .map(this::mapToOrderTable)
        .orElseThrow(IllegalArgumentException::new);
  }

  private List<OrderLineItem> findOrderLineItem(final OrderEntity savedOrderEntity) {
    return orderLineItemDao.findAllByOrderId(savedOrderEntity.getId())
        .stream()
        .map(this::mapToOrderLineItem)
        .collect(Collectors.toList());
  }

  private OrderLineItem mapToOrderLineItem(final OrderLineItemEntity orderLineItemEntity) {
    return new OrderLineItem(orderLineItemEntity.getSeq(),
        orderLineItemEntity.getMenuId(),
        orderLineItemEntity.getQuantity());
  }

  private OrderTable mapToOrderTable(final OrderTableEntity entity) {
    return new OrderTable(
        entity.getId(),
        entity.getTableGroupId(),
        entity.getNumberOfGuests(),
        entity.isEmpty());
  }

  @Override
  public Optional<Order> findById(final Long id) {
    return orderDao.findById(id)
        .map(this::mapToOrder);
  }

  @Override
  public List<Order> findAll() {
    return orderDao.findAll()
        .stream()
        .map(this::mapToOrder)
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
