package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.entity.OrderTableEntity;
import kitchenpos.dao.entity.TableGroupEntity;
import kitchenpos.domain.OrderTable2;
import kitchenpos.domain.TableGroup2;
import kitchenpos.domain.TableGroupRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TableGroupRepositoryImpl implements TableGroupRepository {

  private final TableGroupDao2 tableGroupDao;
  private final OrderTableDao2 orderTableDao;

  public TableGroupRepositoryImpl(final TableGroupDao2 tableGroupDao,
      final OrderTableDao2 orderTableDao) {
    this.tableGroupDao = tableGroupDao;
    this.orderTableDao = orderTableDao;
  }

  @Override
  public TableGroup2 save(final TableGroup2 tableGroup) {
    final TableGroupEntity tableGroupEntity = tableGroupDao.save(
        new TableGroupEntity(
            tableGroup.getCreatedDate()
        )
    );

    List<OrderTable2> savedOrderTables = new ArrayList<>();

    for (final OrderTable2 orderTable : tableGroup.getOrderTables()) {
      final OrderTableEntity savedOrderTableEntity = orderTableDao.save(
          new OrderTableEntity(
              orderTable.getId(),
              tableGroupEntity.getId(),
              orderTable.getNumberOfGuests(),
              orderTable.isEmpty()
          )
      );

      savedOrderTables.add(
          new OrderTable2(
              savedOrderTableEntity.getId(),
              savedOrderTableEntity.getTableGroupId(),
              savedOrderTableEntity.getNumberOfGuests(),
              savedOrderTableEntity.isEmpty()
          )
      );
    }

    return new TableGroup2(
        tableGroupEntity.getId(),
        tableGroupEntity.getCreatedDate(),
        savedOrderTables
    );
  }

  @Override
  public Optional<TableGroup2> findById(final Long id) {
    return Optional.ofNullable(tableGroupDao.findById(id)
        .map(this::mapToTableGroup)
        .orElseThrow(IllegalArgumentException::new));
  }

  private TableGroup2 mapToTableGroup(final TableGroupEntity entity) {
    return new TableGroup2(
        entity.getId(),
        entity.getCreatedDate(),
        orderTableDao.findAllByTableGroupId(entity.getId())
            .stream()
            .map(this::mapToOrderTable)
            .collect(Collectors.toList())
    );
  }

  private OrderTable2 mapToOrderTable(final OrderTableEntity orderTableEntity) {
    return new OrderTable2(
        orderTableEntity.getId(),
        orderTableEntity.getTableGroupId(),
        orderTableEntity.getNumberOfGuests(),
        orderTableEntity.isEmpty()
    );
  }

  @Override
  public List<TableGroup2> findAll() {
    return tableGroupDao.findAll()
        .stream()
        .map(this::mapToTableGroup)
        .collect(Collectors.toList());
  }
}
