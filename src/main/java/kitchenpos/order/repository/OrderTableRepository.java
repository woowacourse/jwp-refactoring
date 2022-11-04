package kitchenpos.order.repository;

import java.util.List;
import kitchenpos.order.domain.OrderTable;

public interface OrderTableRepository {
    OrderTable save(OrderTable entity);

    List<OrderTable> saveAll(List<OrderTable> tables);

    OrderTable findById(Long id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
