package kitchenpos.order.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface OrderRepository extends Repository<Order, Long> {

    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(
            Long orderTableId,
            List<OrderStatus> orderStatuses
    );

    boolean existsByOrderTableIdInAndOrderStatusIn(
            List<Long> orderTableIds,
            List<OrderStatus> orderStatuses
    );

}
