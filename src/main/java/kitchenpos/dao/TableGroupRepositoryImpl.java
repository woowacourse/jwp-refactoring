package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.entity.OrderTableEntity;
import kitchenpos.dao.entity.TableGroupEntity;
import kitchenpos.dao.mapper.OrderTableMapper;
import kitchenpos.dao.mapper.TableGroupMapper;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TableGroupRepositoryImpl implements TableGroupRepository {

  private final TableGroupDao tableGroupDao;
  private final OrderTableDao orderTableDao;

  public TableGroupRepositoryImpl(
      final TableGroupDao tableGroupDao,
      final OrderTableDao orderTableDao
  ) {
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
      savedOrderTables.add(updateOrderTable(orderTable, tableGroupEntity.getId()));
    }

    return TableGroupMapper.mapToTableGroup(tableGroupEntity, savedOrderTables);
  }

  public OrderTable updateOrderTable(final OrderTable orderTable, final Long tableGroupId) {
    final OrderTableEntity orderTableEntity = new OrderTableEntity(
        orderTable.getId(),
        tableGroupId,
        orderTable.getNumberOfGuests(),
        orderTable.isEmpty()
    );

    return OrderTableMapper.mapToOrderTable(orderTableEntity);
  }

  @Override
  public Optional<TableGroup> findById(final Long id) {
    return Optional.ofNullable(tableGroupDao.findById(id)
        .map(entity -> TableGroupMapper.mapToTableGroup(entity, findOrderTables(entity)))
        .orElseThrow(IllegalArgumentException::new));
  }

  private List<OrderTable> findOrderTables(final TableGroupEntity entity) {
    return orderTableDao.findAllByTableGroupId(entity.getId())
        .stream()
        .map(OrderTableMapper::mapToOrderTable)
        .collect(Collectors.toList());
  }

  @Override
  public List<TableGroup> findAll() {
    return tableGroupDao.findAll()
        .stream()
        .map(entity -> TableGroupMapper.mapToTableGroup(entity, findOrderTables(entity)))
        .collect(Collectors.toList());
  }
}
