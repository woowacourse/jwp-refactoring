package kitchenpos.application.dao;

import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.TableGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeTableGroupDao implements TableGroupDao {

    private final Map<Long, TableGroup> tableGroups = new HashMap<>();

    private long id = 1;

    @Override
    public TableGroup save(final TableGroup tableGroup) {
        tableGroup.setId(id);
        tableGroups.put(id++, tableGroup);
        return tableGroup;
    }

    @Override
    public Optional<TableGroup> findById(final Long id) {
        return Optional.ofNullable(tableGroups.get(id));
    }

    @Override
    public List<TableGroup> findAll() {
        return tableGroups.values()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    public void clear() {
        tableGroups.clear();
    }
}
