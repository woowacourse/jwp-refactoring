package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.entity.OrderTableEntity;
import kitchenpos.dao.entity.TableGroupEntity;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
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
  public TableGroup save(final TableGroup tableGroup) {
    final TableGroupEntity tableGroupEntity = tableGroupDao.save(
        new TableGroupEntity(
            tableGroup.getCreatedDate()
        )
    );

    List<OrderTable> savedOrderTables = new ArrayList<>();

    for (final OrderTable orderTable : tableGroup.getOrderTables()) {
      final OrderTableEntity savedOrderTableEntity = orderTableDao.save(
          new OrderTableEntity(
              orderTable.getId(),
              tableGroupEntity.getId(),
              orderTable.getNumberOfGuests(),
              orderTable.isEmpty()
          )
      );

      savedOrderTables.add(
          new OrderTable(
              savedOrderTableEntity.getId(),
              tableGroupEntity.getId(),
              savedOrderTableEntity.getNumberOfGuests(),
              savedOrderTableEntity.isEmpty()
          )
      );
    }

    return new TableGroup(
        tableGroupEntity.getId(),
        tableGroupEntity.getCreatedDate(),
        savedOrderTables
    );
  }

  @Override
  public Optional<TableGroup> findById(final Long id) {
    return Optional.ofNullable(tableGroupDao.findById(id)
        .map(this::mapToTableGroup)
        .orElseThrow(IllegalArgumentException::new));
  }

  private TableGroup mapToTableGroup(final TableGroupEntity entity) {
    return new TableGroup(
        entity.getId(),
        entity.getCreatedDate(),
        orderTableDao.findAllByTableGroupId(entity.getId())
            .stream()
            .map(this::mapToOrderTable)
            .collect(Collectors.toList())
    );
  }

  private OrderTable mapToOrderTable(final OrderTableEntity orderTableEntity) {
    return new OrderTable(
        orderTableEntity.getId(),
        orderTableEntity.getTableGroupId(),
        orderTableEntity.getNumberOfGuests(),
        orderTableEntity.isEmpty()
    );
  }

  @Override
  public List<TableGroup> findAll() {
    return tableGroupDao.findAll()
        .stream()
        .map(this::mapToTableGroup)
        .collect(Collectors.toList());
  }
}
