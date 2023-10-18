package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.entity.OrderEntity;
import kitchenpos.dao.entity.OrderLineItemEntity;
import kitchenpos.dao.entity.OrderTableEntity;
import kitchenpos.domain.Order2;
import kitchenpos.domain.OrderLineItem2;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable2;
import kitchenpos.domain.TableGroup2;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

  private final OrderDao2 orderDao;
  private final OrderTableDao2 orderTableDao;
  private final TableGroupDao2 tableGroupDao;
  private final OrderLineItemDao2 orderLineItemDao;
  private final MenuDao2 menuDao;

  public OrderRepositoryImpl(
      final OrderDao2 orderDao,
      final OrderTableDao2 orderTableDao,
      final TableGroupDao2 tableGroupDao,
      final OrderLineItemDao2 orderLineItemDao, final MenuDao2 menuDao) {
    this.orderDao = orderDao;
    this.orderTableDao = orderTableDao;
    this.tableGroupDao = tableGroupDao;
    this.orderLineItemDao = orderLineItemDao;
    this.menuDao = menuDao;
  }

  @Override
  public Order2 save(final Order2 order) {
    final OrderEntity entity = new OrderEntity(
        order.getOrderTable().getId(),
        order.getOrderStatus().name(),
        order.getOrderedTime()
    );

    final OrderEntity savedOrderEntity = orderDao.save(entity);

    final List<OrderLineItem2> savedOrderLineItems = new ArrayList<>();

    for (final OrderLineItem2 orderLineItem : order.getOrderLineItems()) {
      final OrderLineItemEntity savedEntity = orderLineItemDao.save(
          new OrderLineItemEntity(
              savedOrderEntity.getId(),
              orderLineItem.getMenuId(),
              orderLineItem.getQuantity()
          )
      );

      savedOrderLineItems.add(
          new OrderLineItem2(
              savedEntity.getSeq(),
              orderLineItem.getMenuId(),
              savedEntity.getQuantity()
          )
      );
    }

    return new Order2(
        savedOrderEntity.getId(),
        order.getOrderTable(),
        order.getOrderStatus(),
        order.getOrderedTime(),
        savedOrderLineItems
    );
  }

  private Order2 mapToOrder(final OrderEntity savedOrderEntity) {
    return new Order2(
        savedOrderEntity.getId(),
        findOrderTable(savedOrderEntity),
        OrderStatus.valueOf(savedOrderEntity.getOrderStatus()),
        savedOrderEntity.getOrderedTime(),
        findOrderLineItem(savedOrderEntity)
    );
  }

  private OrderTable2 findOrderTable(final OrderEntity savedOrderEntity) {
    return orderTableDao.findById(savedOrderEntity.getOrderTableId())
        .map(this::mapToOrderTable)
        .orElseThrow(IllegalArgumentException::new);
  }

  private List<OrderLineItem2> findOrderLineItem(final OrderEntity savedOrderEntity) {
    return orderLineItemDao.findAllByOrderId(savedOrderEntity.getId())
        .stream()
        .map(this::mapToOrderLineItem)
        .collect(Collectors.toList());
  }

  private OrderLineItem2 mapToOrderLineItem(final OrderLineItemEntity orderLineItemEntity) {
    return new OrderLineItem2(orderLineItemEntity.getSeq(),
        orderLineItemEntity.getMenuId(),
        orderLineItemEntity.getQuantity());
  }

  private OrderTable2 mapToOrderTable(final OrderTableEntity entity) {
    return new OrderTable2(
        entity.getId(),
        entity.getTableGroupId(),
        entity.getNumberOfGuests(),
        entity.isEmpty());
  }

  @Override
  public Optional<Order2> findById(final Long id) {
    return orderDao.findById(id)
        .map(this::mapToOrder);
  }

  @Override
  public List<Order2> findAll() {
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
