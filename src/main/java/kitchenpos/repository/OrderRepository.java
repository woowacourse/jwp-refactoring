package kitchenpos.repository;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    default Order getById(final Long id) {
        return findById(id).orElseThrow(IllegalArgumentException::new);
    }
    boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId,final List<OrderStatus> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds,final List<OrderStatus> orderStatuses);
}
