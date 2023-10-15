package kitchenpos.fake;

import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryTableGroupDao implements TableGroupRepository {

    private final Map<Long, TableGroup> map = new HashMap<>();
    private final AtomicLong id = new AtomicLong(0);

    @Override
    public TableGroup save(TableGroup entity) {
        if (Objects.isNull(entity.getId())) {
            long id = this.id.getAndIncrement();
            TableGroup tableGroup = new TableGroup(id, entity.getCreatedDate(), entity.getOrderTables());
            map.put(id, tableGroup);
            return tableGroup;
        }
        map.put(entity.getId(), entity);
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
