package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdAndOrderStatus(final Long orderTableId, final String orderStatus);

    boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds, final List<String> orderStauts);
}
