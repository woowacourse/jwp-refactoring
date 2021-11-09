package kitchenpos.order.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> status);

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> status);
}
