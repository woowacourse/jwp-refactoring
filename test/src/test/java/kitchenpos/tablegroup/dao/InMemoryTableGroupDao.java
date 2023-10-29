package kitchenpos.tablegroup.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.repository.TableGroupDao;

public class InMemoryTableGroupDao implements TableGroupDao {

    private final List<TableGroup> tableGroups = new ArrayList<>();

    @Override
    public TableGroup save(final TableGroup entity) {
        final var id = (long) (tableGroups.size() + 1);
        final var saved = new TableGroup(id, entity.getOrderTables(), entity.getCreatedDate());
        tableGroups.add(saved);
        return saved;
    }

    @Override
    public Optional<TableGroup> findById(final Long id) {
        return tableGroups.stream()
                          .filter(tableGroup -> tableGroup.getId().equals(id))
                          .findAny();
    }

    @Override
    public List<TableGroup> findAll() {
        return tableGroups;
    }
}
