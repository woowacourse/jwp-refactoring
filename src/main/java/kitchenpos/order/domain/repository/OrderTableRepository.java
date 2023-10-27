package kitchenpos.order.domain.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.OrderTable;

public interface OrderTableRepository {

  OrderTable save(OrderTable orderTable);

  Optional<OrderTable> findById(Long id);

  List<OrderTable> findAll();

  List<OrderTable> findAllByIdIn(List<Long> ids);

  List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
