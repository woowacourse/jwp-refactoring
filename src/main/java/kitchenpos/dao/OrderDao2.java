package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.entity.OrderEntity;
import kitchenpos.domain.Order;

public interface OrderDao2 {
    OrderEntity save(OrderEntity entity);

    Optional<OrderEntity> findById(Long id);

    List<OrderEntity> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);
}
