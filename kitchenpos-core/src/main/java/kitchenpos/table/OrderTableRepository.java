package kitchenpos.table;

import java.util.List;
import java.util.Optional;

public interface OrderTableRepository {

    OrderTable save(final OrderTable entity);

    Optional<OrderTable> findById(final Long id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(final List<Long> ids);

    List<OrderTable> findAllByTableGroupId(final Long tableGroupId);

    default OrderTable getById(final Long id) {
        return findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }
}
