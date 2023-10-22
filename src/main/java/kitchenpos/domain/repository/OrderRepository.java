package kitchenpos.domain.repository;

import java.util.List;
import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<String> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds, final List<String> orderStatuses);
}
