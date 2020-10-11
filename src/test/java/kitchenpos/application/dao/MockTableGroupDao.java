package kitchenpos.application.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.TableGroup;

public class MockTableGroupDao implements TableGroupDao {

    private Map<Long, TableGroup> tableGroups = new HashMap<>();
    private Long id = 1L;

    @Override
    public TableGroup save(TableGroup entity) {
        if (Objects.isNull(entity.getId())) {
            entity.setId(id++);
        }
        tableGroups.put(entity.getId(), entity);
        return entity;
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
