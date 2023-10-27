package kitchenpos.order.persistence;

import kitchenpos.order.persistence.entity.OrderEntity;

import java.util.List;
import java.util.Optional;

public interface OrderDao {

    OrderEntity save(OrderEntity entity);

    Optional<OrderEntity> findById(Long id);

    List<OrderEntity> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds,
                                                   List<String> orderStatuses);
}
