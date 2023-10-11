package kitchenpos.fake;

import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.TableGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryTableGroupDao implements TableGroupDao {

    private final Map<Long, TableGroup> map = new HashMap<>();
    private final AtomicLong id = new AtomicLong(0);

    @Override
    public TableGroup save(TableGroup entity) {
        long id = this.id.getAndIncrement();
        entity.setId(id);
        map.put(id, entity);
        return entity;
    }

    @Override
    public Optional<TableGroup> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<TableGroup> findAll() {
        return new ArrayList<>(map.values());
    }
}
