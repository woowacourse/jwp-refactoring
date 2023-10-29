package kitchenpos.ordertable.domain.repository;

import kitchenpos.ordertable.domain.OrderTable;

import java.util.List;
import java.util.Optional;

public interface OrderTableRepository {

    OrderTable save(OrderTable entity);

    Optional<OrderTable> findById(Long id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdsIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);

    List<OrderTable> saveAll(List<OrderTable> orderTables);
}
