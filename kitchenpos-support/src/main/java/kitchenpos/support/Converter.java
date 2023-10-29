package kitchenpos.support;

import java.util.List;
import java.util.stream.Collectors;

public interface Converter<E, D> {

    D entityToData(final E entity);

    E dataToEntity(final D data);

    default List<E> dataToEntity(final List<D> data) {
        return data.stream()
                .map(this::dataToEntity)
                .collect(Collectors.toList());
    }
}
