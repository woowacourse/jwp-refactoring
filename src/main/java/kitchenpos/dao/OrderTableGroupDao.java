package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTableGroup;

public interface OrderTableGroupDao {
    OrderTableGroup save(OrderTableGroup entity);

    Optional<OrderTableGroup> findById(Long id);

    List<OrderTableGroup> findAll();

    boolean existsById(Long orderTableGroupId);
}
