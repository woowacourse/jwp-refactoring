package kitchenpos.application.dao;

import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

public class TestOrderTableDao implements OrderTableDao {

    protected final AtomicLong incrementId;
    protected final Map<Long, OrderTable> database;

    public TestOrderTableDao() {
        this.incrementId = new AtomicLong(1L);
        this.database = new TreeMap<>();
    }

    public TestOrderTableDao(AtomicLong incrementId, Map<Long, OrderTable> database) {
        this.incrementId = incrementId;
        this.database = database;
    }

    public OrderTable save(OrderTable entity) {
        long entityId =
            Objects.nonNull(entity.getId()) ?
                entity.getId() :
                incrementId.getAndIncrement();
        entity.setId(entityId);
        database.put(entityId, entity);
        return entity;

    }

    public Optional<OrderTable> findById(Long id) {
        return Optional.ofNullable(database.get(id));
    }

    public List<OrderTable> findAll() {
        return database.values().stream()
            .sorted(Comparator.comparingLong(OrderTable::getId))
            .collect(toList());
    }

    public long countByIdIn(List<Long> ids) {
        return ids.stream()
            .filter(database::containsKey)
            .count();
    }

    public boolean existsById(Long id) {
        return database.containsKey(id);
    }

    public List<OrderTable> findAllByIdIn(List<Long> ids) {
        return database.values().stream()
            .filter(orderTable -> ids.contains(orderTable.getId()))
            .collect(toList());
    }

    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return database.values().stream()
            .filter(orderTable -> tableGroupId.equals(orderTable.getTableGroupId()))
            .collect(toList());
    }
}
