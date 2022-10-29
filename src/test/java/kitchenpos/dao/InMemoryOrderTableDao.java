package kitchenpos.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import org.springframework.util.ReflectionUtils;

public class InMemoryOrderTableDao implements OrderTableDao {

    protected final AtomicLong id;
    protected final Map<Long, OrderTable> database;

    public InMemoryOrderTableDao() {
        this.id = new AtomicLong(1L);
        this.database = new TreeMap<>();
    }

    public InMemoryOrderTableDao(final AtomicLong id, final Map<Long, OrderTable> database) {
        this.id = id;
        this.database = database;
    }

    @Override
    public OrderTable save(final OrderTable entity) {
        long entityId = checkEntityIdCondition(entity);
        final Field field = ReflectionUtils.findField(OrderTable.class, "id");
        field.setAccessible(true);
        ReflectionUtils.setField(field, entity, entityId);
        database.put(entityId, entity);
        return entity;
    }

    private long checkEntityIdCondition(final OrderTable entity) {
        if (Objects.nonNull(entity.getId())) {
            return entity.getId();
        }
        return id.getAndIncrement();
    }

    @Override
    public Optional<OrderTable> findById(final Long id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public List<OrderTable> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public List<OrderTable> findAllByIdIn(final List<Long> ids) {
        return database.values()
                .stream()
                .filter(orderTable -> ids.contains(orderTable.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(final Long tableGroupId) {
        return database.values()
                .stream()
                .filter(orderTable -> tableGroupId.equals(orderTable.getTableGroupId()))
                .collect(Collectors.toList());
    }
}
