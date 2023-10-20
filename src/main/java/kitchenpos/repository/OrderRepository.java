package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdAndOrderStatusIn(Long id, List<OrderStatus> orderStatus);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> id, List<OrderStatus> orderStatus);
}
