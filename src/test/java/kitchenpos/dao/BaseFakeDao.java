package kitchenpos.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public abstract class BaseFakeDao<T> {

    protected final HashMap<Long, T> entities = new HashMap<>();
    private Long key = 1L;

    public T save(T entity) {
        try {
            setPrimaryKey(entity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        entities.put(key, entity);
        key += 1;
        return entity;
    }

    public Optional<T> findById(Long id) {
        return Optional.ofNullable(entities.get(id));
    }

    public List<T> findAll() {
        return new ArrayList<>(entities.values());
    }

    private void setPrimaryKey(final T entity) throws IllegalAccessException, InvocationTargetException {
        final Class<?> entityClass = entity.getClass();
        final var setId = setIdMethod(entityClass);
        if (setId.isPresent()) {
            setId.get().invoke(entity, key);
            return;
        }
        final var setSeq = setSeqMethod(entityClass);
        if (setSeq.isPresent()) {
            setSeq.get().invoke(entity, key);
            return;
        }
        throw new RuntimeException();
    }

    private Optional<Method> setIdMethod(final Class<?> entityClass) {
        try {
            return Optional.of(entityClass.getMethod("setId", Long.class));
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    private Optional<Method> setSeqMethod(final Class<?> entityClass) {
        try {
            return Optional.of(entityClass.getMethod("setSeq", Long.class));
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }
}
