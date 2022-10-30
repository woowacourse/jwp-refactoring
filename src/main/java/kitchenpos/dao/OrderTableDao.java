package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.OrderTable;

public interface OrderTableDao {
    OrderTable save(OrderTable entity);

    OrderTable findById(Long id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
