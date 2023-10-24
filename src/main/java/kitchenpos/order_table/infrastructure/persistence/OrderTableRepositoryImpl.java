package kitchenpos.order_table.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.order_table.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrderTableRepositoryImpl implements OrderTableRepository {

  private final OrderTableDao orderTableDao;

  public OrderTableRepositoryImpl(final OrderTableDao orderTableDao) {
    this.orderTableDao = orderTableDao;
  }

  @Override
  public OrderTable save(final OrderTable orderTable) {
    final OrderTableEntity entity = new OrderTableEntity(
        orderTable.getId(),
        orderTable.getTableGroupId(),
        orderTable.getNumberOfGuests(),
        orderTable.isEmpty()
    );

    final OrderTableEntity savedEntity = orderTableDao.save(entity);

    return OrderTableMapper.mapToOrderTable(savedEntity);
  }

  @Override
  public Optional<OrderTable> findById(final Long id) {
    return orderTableDao.findById(id)
        .map(OrderTableMapper::mapToOrderTable);
  }

  @Override
  public List<OrderTable> findAll() {
    return orderTableDao.findAll()
        .stream()
        .map(OrderTableMapper::mapToOrderTable)
        .collect(Collectors.toList());
  }

  @Override
  public List<OrderTable> findAllByIdIn(final List<Long> ids) {
    return orderTableDao.findAllByIdIn(ids)
        .stream()
        .map(OrderTableMapper::mapToOrderTable)
        .collect(Collectors.toList());
  }

  @Override
  public List<OrderTable> findAllByTableGroupId(final Long tableGroupId) {
    return orderTableDao.findAllByTableGroupId(tableGroupId)
        .stream()
        .map(OrderTableMapper::mapToOrderTable)
        .collect(Collectors.toList());
  }
}
