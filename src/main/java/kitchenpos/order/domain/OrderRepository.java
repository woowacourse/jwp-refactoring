package kitchenpos.order.domain;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order save(Order entity);

    Optional<Order> findById(Long id);

    Optional<Order> findByTableId(Long id);

    List<Order> findAll();

    List<Order> findAllByOrderTableId(List<Long> orderTableId);

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);
}
