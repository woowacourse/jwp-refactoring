package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.springframework.data.repository.Repository;

public interface OrderRepository extends Repository<Order, Long>, OrderEntityRepository {
    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);
}
