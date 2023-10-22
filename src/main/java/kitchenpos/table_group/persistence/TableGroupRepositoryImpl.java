package kitchenpos.table_group.persistence;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.order_table.application.entity.OrderTableEntity;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.order_table.persistence.OrderTableDao;
import kitchenpos.table_group.application.entity.TableGroupEntity;
import kitchenpos.table_group.domain.TableGroup;
import kitchenpos.table_group.domain.repository.TableGroupRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TableGroupRepositoryImpl implements TableGroupRepository {

  private final TableGroupDao tableGroupDao;
  private final OrderTableDao orderTableDao;

  public TableGroupRepositoryImpl(final TableGroupDao tableGroupDao,
      final OrderTableDao orderTableDao) {
    this.tableGroupDao = tableGroupDao;
    this.orderTableDao = orderTableDao;
  }

  @Override
  public TableGroup save(final TableGroup entity) {
    final TableGroupEntity savedTableGroup = tableGroupDao.save(
        TableGroupEntity.from(new TableGroup(LocalDateTime.now())));

    final List<OrderTable> savedOrderTables = new ArrayList<>();
    for (final OrderTable orderTable : entity.getOrderTables()) {
      savedOrderTables.add(
          orderTableDao.save(new OrderTableEntity(orderTable.getId(), savedTableGroup.getId(),
              orderTable.getNumberOfGuests(), false)).toOrderTable());
    }

    return savedTableGroup.toTableGroup(savedOrderTables);
  }

  @Override
  public Optional<TableGroup> findById(final Long id) {
    return tableGroupDao.findById(id).map(TableGroupEntity::toTableGroup);
  }// TODO: 2023/10/22 orderTables 값 채워주기

  @Override
  public List<TableGroup> findAll() {
    return tableGroupDao.findAll()
        .stream()
        .map(TableGroupEntity::toTableGroup)
        .collect(Collectors.toList());
  }
}
