package kitchenpos.dao;

import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;

abstract class TestAbstractDao<T> {
    public final AtomicLong incrementId;
    public final Map<Long, T> database;

    public TestAbstractDao() {
        this.incrementId = new AtomicLong(1L);
        this.database = new TreeMap<>();
    }
    
    public TestAbstractDao(AtomicLong incrementId, Map<Long, T> database) {
        this.incrementId = incrementId;
        this.database = database;
    }

    public T save(T entity){
        long entityId = incrementId.getAndIncrement();
        setIdConsumer().accept(entity, entityId);
        database.put(entityId, entity);
        return entity;

    };

    protected abstract BiConsumer<T, Long> setIdConsumer();

    public Optional<T> findById(Long id) {
        return Optional.ofNullable(database.get(id));
    }

    public List<T> findAll(){
        return database.values().stream()
            .sorted(comparatorForSort())
            .collect(toList());
    }

    protected abstract Comparator<T> comparatorForSort();

    public long countByIdIn(List<Long> ids) {
        return ids.stream()
            .filter(database::containsKey)
            .count();
    }

    public boolean existsById(Long id){
        return database.containsKey(id);
    }
}
