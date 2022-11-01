package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.exception.notfound.OrderNotFoundException;
import org.springframework.data.repository.Repository;

public interface OrderRepository extends Repository<Order, Long> {
    Order save(Order entity);

    default Order getById(Long id) {
        return findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    Optional<Order> findById(Long id);

    List<Order> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);
}
