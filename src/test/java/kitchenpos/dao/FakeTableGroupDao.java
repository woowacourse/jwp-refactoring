package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.TableGroup;

public class FakeTableGroupDao implements TableGroupDao {

    private final List<TableGroup> IN_MEMORY_TABLE_GROUP;
    private Long id;

    public FakeTableGroupDao() {
        IN_MEMORY_TABLE_GROUP = new ArrayList<>();
        id = 1L;
    }

    @Override
    public TableGroup save(TableGroup entity) {
        TableGroup tableGroup = new TableGroup(id++, entity.getCreatedDate(), entity.getOrderTables());
        IN_MEMORY_TABLE_GROUP.add(tableGroup);
        return tableGroup;
    }

    @Override
    public Optional<TableGroup> findById(Long id) {
        return IN_MEMORY_TABLE_GROUP.stream()
                .filter(tableGroup -> tableGroup.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<TableGroup> findAll() {
        return new ArrayList<>(IN_MEMORY_TABLE_GROUP);
    }
}
