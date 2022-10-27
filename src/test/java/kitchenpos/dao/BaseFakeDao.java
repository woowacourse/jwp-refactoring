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
        final var primaryKey = findPrimaryKey(entity);
        if (primaryKey.isEmpty()) {
            setPrimaryKey(entity);
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

    private Optional<Long> findPrimaryKey(final T entity) {
        final Class<?> entityClass = entity.getClass();
        final var getId = findGetId(entityClass);
        if (getId.isPresent()) {
            try {
                return Optional.ofNullable((Long) getId.get().invoke(entity));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        final var getSeq = findGetSeq(entityClass);
        if (getSeq.isPresent()) {
            try {
                return Optional.ofNullable((Long) getSeq.get().invoke(entity));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException();
    }

    private void setPrimaryKey(final T entity) {
        final Class<?> entityClass = entity.getClass();
        final var setId = findSetId(entityClass);
        if (setId.isPresent()) {
            try {
                setId.get().invoke(entity, key);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return;
        }
        final var setSeq = findSetSeq(entityClass);
        if (setSeq.isPresent()) {
            try {
                setSeq.get().invoke(entity, key);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return;
        }
        throw new RuntimeException();
    }

    private Optional<Method> findGetId(final Class<?> entityClass) {
        try {
            return Optional.of(entityClass.getMethod("getId"));
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    private Optional<Method> findGetSeq(final Class<?> entityClass) {
        try {
            return Optional.of(entityClass.getMethod("getSeq"));
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    private Optional<Method> findSetId(final Class<?> entityClass) {
        try {
            return Optional.of(entityClass.getMethod("setId", Long.class));
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    private Optional<Method> findSetSeq(final Class<?> entityClass) {
        try {
            return Optional.of(entityClass.getMethod("setSeq", Long.class));
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }
}
