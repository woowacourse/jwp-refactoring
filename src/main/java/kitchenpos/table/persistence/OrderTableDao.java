package kitchenpos.table.persistence;

import java.util.List;
import java.util.Optional;
import kitchenpos.table.application.entity.OrderTableEntity;

public interface OrderTableDao {

  OrderTableEntity save(OrderTableEntity entity);

  Optional<OrderTableEntity> findById(Long id);

  List<OrderTableEntity> findAll();

  List<OrderTableEntity> findAllByIdIn(List<Long> ids);

  List<OrderTableEntity> findAllByTableGroupId(Long tableGroupId);
}
