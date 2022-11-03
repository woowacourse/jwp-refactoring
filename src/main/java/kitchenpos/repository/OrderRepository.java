package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.order.Order;
import kitchenpos.repository.entity.OrderEntityRepository;
import org.springframework.data.repository.Repository;

public interface OrderRepository extends Repository<Order, Long>, OrderEntityRepository {
    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);
}
