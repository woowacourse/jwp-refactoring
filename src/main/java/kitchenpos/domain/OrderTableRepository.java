package kitchenpos.domain;

import java.util.List;
import java.util.Optional;

public interface OrderTableRepository {

  OrderTable2 save(OrderTable2 orderTable);

  Optional<OrderTable2> findById(Long id);

  List<OrderTable2> findAll();

  List<OrderTable2> findAllByIdIn(List<Long> ids);

  List<OrderTable2> findAllByTableGroupId(Long tableGroupId);
}
