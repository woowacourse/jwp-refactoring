package kitchenpos.dao;

import kitchenpos.domain.Order;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface OrderRepository extends Repository<Order, Long> {
    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);
}
