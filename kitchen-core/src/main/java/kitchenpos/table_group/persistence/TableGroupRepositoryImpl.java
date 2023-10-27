package kitchenpos.table_group.persistence;

import kitchenpos.table.persistence.OrderTableDao;
import kitchenpos.table_group.domain.TableGroup;
import kitchenpos.table_group.domain.repository.TableGroupRepository;
import kitchenpos.table_group.persistence.entity.TableGroupEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

        return savedTableGroup.toTableGroup();
    }

    @Override
    public Optional<TableGroup> findById(final Long id) {
        return tableGroupDao.findById(id).map(TableGroupEntity::toTableGroup);
    }

    @Override
    public List<TableGroup> findAll() {
        return tableGroupDao.findAll()
                .stream()
                .map(TableGroupEntity::toTableGroup)
                .collect(Collectors.toList());
    }
}
