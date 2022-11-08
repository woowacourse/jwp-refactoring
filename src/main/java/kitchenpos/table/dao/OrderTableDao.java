package kitchenpos.table.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.table.domain.OrderTable;

public interface OrderTableDao {

    OrderTable save(OrderTable entity);

    OrderTable save(OrderTable entity, Long tableGroupId);

    Optional<OrderTable> findById(Long id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);

    Long findTableGroupIdById(Long id);

    default OrderTable getById(Long id) {
        return findById(id).orElseThrow(IllegalArgumentException::new);
    }
}
