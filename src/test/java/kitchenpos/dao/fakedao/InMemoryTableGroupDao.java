package kitchenpos.dao.fakedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.TableGroup;

public class InMemoryTableGroupDao implements TableGroupDao {

    private final List<TableGroup> tableGroups = new ArrayList<>();

    @Override
    public TableGroup save(final TableGroup entity) {
        entity.setId((long) (tableGroups.size() + 1));
        tableGroups.add(entity);
        return entity;
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
