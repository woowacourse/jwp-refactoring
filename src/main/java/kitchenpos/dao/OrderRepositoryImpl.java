package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.entity.OrderEntity;
import kitchenpos.dao.entity.OrderTableEntity;
import kitchenpos.domain.Order2;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable2;
import kitchenpos.domain.TableGroup2;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

  private final OrderDao2 orderDao;
  private final OrderTableDao2 orderTableDao;
  private final TableGroupDao2 tableGroupDao;

  public OrderRepositoryImpl(
      final OrderDao2 orderDao,
      final OrderTableDao2 orderTableDao,
      final TableGroupDao2 tableGroupDao
  ) {
    this.orderDao = orderDao;
    this.orderTableDao = orderTableDao;
    this.tableGroupDao = tableGroupDao;
  }

  @Override
  public Order2 save(final Order2 order) {
    final OrderEntity entity = new OrderEntity(
        order.getOrderTable().getId(),
        order.getOrderStatus(),
        order.getOrderedTime()
    );

    final OrderEntity savedOrderEntity = orderDao.save(entity);

    return mapToOrder(savedOrderEntity);
  }

  private Order2 mapToOrder(final OrderEntity savedOrderEntity) {
    return new Order2(
        savedOrderEntity.getId(),
        orderTableDao.findById(savedOrderEntity.getOrderTableId())
            .map(this::mapToOrderTable)
            .orElseThrow(IllegalArgumentException::new),
        savedOrderEntity.getOrderStatus(),
        savedOrderEntity.getOrderedTime(),
        null
    );
  }

  private OrderTable2 mapToOrderTable(final OrderTableEntity entity) {
    return new OrderTable2(
        entity.getId(),
        mapToTableGroup(entity),
        entity.getNumberOfGuests(),
        entity.isEmpty());
  }

  private TableGroup2 mapToTableGroup(final OrderTableEntity orderTableEntity) {
    return tableGroupDao.findById(orderTableEntity.getTableGroupId())
        .map(entity -> new TableGroup2(
            entity.getId(),
            entity.getCreatedDate()))
        .orElse(null);
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
