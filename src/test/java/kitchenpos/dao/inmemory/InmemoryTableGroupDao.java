package kitchenpos.dao.inmemory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.TableGroup;

public class InmemoryTableGroupDao implements TableGroupDao {

    private final Map<Long, TableGroup> tableGroups;
    private long idValue;

    public InmemoryTableGroupDao() {
        idValue = 0;
        tableGroups = new LinkedHashMap<>();
    }

    @Override
    public TableGroup save(TableGroup entity) {
        long savedId = idValue;
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(savedId);
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(new ArrayList<>(entity.getOrderTables()));
        this.tableGroups.put(savedId, tableGroup);
        idValue++;
        return tableGroup;
    }

    @Override
    public Optional<TableGroup> findById(Long id) {
        return Optional.ofNullable(tableGroups.get(id));
    }

    @Override
    public List<TableGroup> findAll() {
        return new ArrayList<>(tableGroups.values());
    }

}
