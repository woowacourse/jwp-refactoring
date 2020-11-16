package kitchenpos.inmemorydao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.TableGroup;

public class InMemoryTableGroupDao implements TableGroupDao {
    private Map<Long, TableGroup> tableGroups;
    private long index;

    public InMemoryTableGroupDao() {
        this.tableGroups = new HashMap<>();
        this.index = 0;
    }

    @Override
    public TableGroup save(final TableGroup entity) {
        Long key = entity.getId();

        if (key == null) {
            key = index++;
        }

        final TableGroup tableGroup = new TableGroup();
        tableGroup.setId(key);
        tableGroup.setCreatedDate(entity.getCreatedDate());
        tableGroup.setOrderTables(entity.getOrderTables());

        tableGroups.put(key, tableGroup);
        return tableGroup;
    }

    @Override
    public Optional<TableGroup> findById(final Long id) {
        return Optional.of(tableGroups.get(id));
    }

    @Override
    public List<TableGroup> findAll() {
        return new ArrayList<>(tableGroups.values());
    }
}
