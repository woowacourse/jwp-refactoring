package kitchenpos.table.repository;

import java.util.List;
import kitchenpos.table.domain.OrderTable;

public interface OrderTableRepository {
    OrderTable save(OrderTable entity);

    List<OrderTable> saveAll(List<OrderTable> tables);

    OrderTable findById(Long id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
