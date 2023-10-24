package kitchenpos.order_table.domain.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.order_table.domain.OrderTable;

public interface OrderTableRepository {

  OrderTable save(OrderTable orderTable);

  Optional<OrderTable> findById(Long id);

  List<OrderTable> findAll();

  List<OrderTable> findAllByIdIn(List<Long> ids);

  List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
