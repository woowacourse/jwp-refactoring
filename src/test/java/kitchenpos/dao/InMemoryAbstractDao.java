package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

abstract class InMemoryAbstractDao<T> {

    protected final AtomicLong id;
    protected final Map<Long, T> database;

    public InMemoryAbstractDao() {
        this.id = new AtomicLong(1L);
        this.database = new TreeMap<>();
    }

    public InMemoryAbstractDao(final AtomicLong id, final Map<Long, T> database) {
        this.id = id;
        this.database = database;
    }

    public T save(T entity) {
        final long entityId = id.getAndIncrement();
        setId(entity, entityId);
        database.put(entityId, entity);
        return entity;
    }
    protected abstract void setId(T entity, Long entityId);

    public Optional<T> findById(Long id) {
        return Optional.ofNullable(database.get(id));
    }

    public List<T> findAll() {
        return new ArrayList<>(database.values());
    }
}
