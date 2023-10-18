package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.entity.OrderTableEntity;
import kitchenpos.domain.OrderTable2;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup2;
import org.springframework.stereotype.Repository;

@Repository
public class OrderTableRepositoryImpl implements OrderTableRepository {

  private final OrderTableDao2 orderTableDao;
  private final TableGroupDao2 tableGroupDao;

  public OrderTableRepositoryImpl(
      final OrderTableDao2 orderTableDao,
      final TableGroupDao2 tableGroupDao
  ) {
    this.orderTableDao = orderTableDao;
    this.tableGroupDao = tableGroupDao;
  }

  @Override
  public OrderTable2 save(final OrderTable2 orderTable) {
    final OrderTableEntity entity = new OrderTableEntity(
        null,
        orderTable.getNumberOfGuests(),
        orderTable.isEmpty()
    );

    final OrderTableEntity savedEntity = orderTableDao.save(entity);

    return mapToOrderTable(savedEntity);
  }

  private OrderTable2 mapToOrderTable(final OrderTableEntity entity) {
    return new OrderTable2(
        entity.getId(),
        mapToTableGroupFrom(entity),
        entity.getNumberOfGuests(),
        entity.isEmpty()
    );
  }

  private TableGroup2 mapToTableGroupFrom(final OrderTableEntity savedEntity) {
    return tableGroupDao.findById(savedEntity.getTableGroupId())
        .map(entity -> new TableGroup2(entity.getId(), entity.getCreatedDate()))
        .orElse(null);
  }

  @Override
  public Optional<OrderTable2> findById(final Long id) {
    return orderTableDao.findById(id)
        .map(this::mapToOrderTable);
  }

  @Override
  public List<OrderTable2> findAll() {
    return orderTableDao.findAll()
        .stream()
        .map(this::mapToOrderTable)
        .collect(Collectors.toList());
  }

  @Override
  public List<OrderTable2> findAllByIdIn(final List<Long> ids) {
    return orderTableDao.findAllByIdIn(ids)
        .stream()
        .map(this::mapToOrderTable)
        .collect(Collectors.toList());
  }

  @Override
  public List<OrderTable2> findAllByTableGroupId(final Long tableGroupId) {
    return orderTableDao.findAllByTableGroupId(tableGroupId)
        .stream()
        .map(this::mapToOrderTable)
        .collect(Collectors.toList());
  }
}
