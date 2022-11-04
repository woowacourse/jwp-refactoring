package kitchenpos.order.domain;

import java.util.List;
import java.util.Optional;

public interface OrderDao {

    Order save(final Order entity);

    Optional<Order> findById(final Long id);

    List<Order> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<String> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds, final List<String> orderStatuses);
}
