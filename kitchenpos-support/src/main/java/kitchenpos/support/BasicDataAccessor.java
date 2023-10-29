package kitchenpos.support;

import java.util.List;
import java.util.Optional;

public interface BasicDataAccessor<T> {

    T save(final T data);

    Optional<T> findById(final Long id);

    List<T> findAll();
}
