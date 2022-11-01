package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.order.OrderTable;

public interface OrderTableRepository {
    OrderTable save(OrderTable entity);

    Optional<OrderTable> findById(Long id);

    List<OrderTable> findAll();
}
