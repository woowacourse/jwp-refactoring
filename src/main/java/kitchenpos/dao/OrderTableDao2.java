package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.entity.OrderTableEntity;
import kitchenpos.domain.OrderTable;

public interface OrderTableDao2 {
    OrderTableEntity save(OrderTableEntity entity);

    Optional<OrderTableEntity> findById(Long id);

    List<OrderTableEntity> findAll();

    List<OrderTableEntity> findAllByIdIn(List<Long> ids);

    List<OrderTableEntity> findAllByTableGroupId(Long tableGroupId);
}
