package kitchenpos.dao;

import kitchenpos.domain.table.OrderTable;

import java.util.List;
import java.util.Optional;

public interface OrderTableDao {
    OrderTable save(OrderTable entity);

    Optional<OrderTable> findById(OrderTable id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
