package kitchenpos.dao.order;

import java.util.List;
import kitchenpos.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderTableId(Long orderTableId);
}
