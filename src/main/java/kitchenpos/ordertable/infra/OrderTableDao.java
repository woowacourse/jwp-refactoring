package kitchenpos.ordertable.infra;

import java.util.List;
import java.util.Optional;
import kitchenpos.ordertable.domain.OrderTable;

public interface OrderTableDao {
    OrderTable save(OrderTable entity);

    Optional<OrderTable> findById(Long id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
