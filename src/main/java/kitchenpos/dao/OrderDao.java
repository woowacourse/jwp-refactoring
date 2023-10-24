package kitchenpos.dao;

import kitchenpos.domain.Order;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderStatus;
import org.springframework.data.repository.Repository;

public interface OrderDao extends Repository<Order, Long> {
    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatuses);
}
