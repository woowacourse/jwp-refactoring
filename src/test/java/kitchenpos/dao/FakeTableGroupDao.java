package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.TableGroup;

public class FakeTableGroupDao implements TableGroupDao {

    private final List<TableGroup> IN_MEMORY_TABLE_GROUP;

    public FakeTableGroupDao() {
        IN_MEMORY_TABLE_GROUP = new ArrayList<>();
    }

    @Override
    public TableGroup save(TableGroup entity) {
        IN_MEMORY_TABLE_GROUP.add(entity);
        Long id = (long) IN_MEMORY_TABLE_GROUP.size();
        entity.setId(id);
        return entity;
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
