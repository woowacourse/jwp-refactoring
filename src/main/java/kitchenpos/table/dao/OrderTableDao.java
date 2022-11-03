package kitchenpos.table.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;

public interface OrderTableDao {

    OrderTable save(OrderTable entity);

    Optional<OrderTable> findById(Long id);

    List<OrderTable> findAll();

    OrderTables findAllByIdIn(List<Long> ids);

    OrderTables findAllByTableGroupId(Long tableGroupId);

    void saveAll(List<OrderTable> entities);
}
