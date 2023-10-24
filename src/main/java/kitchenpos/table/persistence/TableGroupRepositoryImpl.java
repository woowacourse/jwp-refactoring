package kitchenpos.table.persistence;

import kitchenpos.table.application.entity.OrderTableEntity;
import kitchenpos.table.application.entity.TableGroupEntity;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.repository.TableGroupRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        for (final OrderTable orderTable : entity.getOrderTables().getOrderTables()) {
            savedOrderTables.add(
                    orderTableDao.save(new OrderTableEntity(orderTable.getId(), savedTableGroup.getId(),
                            orderTable.getNumberOfGuests().getValue(), false)).toOrderTable()
            );
        }

        return savedTableGroup.toTableGroup(new OrderTables(savedOrderTables));
    }

    @Override
    public Optional<TableGroup> findById(final Long id) {
        final OrderTables orderTables = new OrderTables(orderTableDao.findAllByTableGroupId(id)
                .stream()
                .map(OrderTableEntity::toOrderTable)
                .collect(Collectors.toList()));
        return tableGroupDao.findById(id).map(entity -> entity.toTableGroup(orderTables));
    }

    @Override
    public List<TableGroup> findAll() {
        return tableGroupDao.findAll()
                .stream()
                .map(entity -> {
                    final OrderTables orderTables = new OrderTables(
                            orderTableDao.findAllByTableGroupId(entity.getId())
                                    .stream()
                                    .map(OrderTableEntity::toOrderTable)
                                    .collect(Collectors.toList()));
                    return entity.toTableGroup(orderTables);
                })
                .collect(Collectors.toList());
    }
}
