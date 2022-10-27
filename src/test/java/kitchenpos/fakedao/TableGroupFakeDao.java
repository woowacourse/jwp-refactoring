package kitchenpos.fakedao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.TableGroup;

public class TableGroupFakeDao implements TableGroupDao {

    private Long autoIncrementId = 0L;
    private final Map<Long, TableGroup> repository = new HashMap<>();

    @Override
    public TableGroup save(TableGroup entity) {
        repository.putIfAbsent(++autoIncrementId, entity);
        entity.setId(autoIncrementId);
        return entity;
    }

    @Override
    public Optional<TableGroup> findById(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public List<TableGroup> findAll() {
        return new ArrayList<>(repository.values());
    }
}
