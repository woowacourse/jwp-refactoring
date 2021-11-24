package kitchenpos.domain.repository;

import java.util.List;
import kitchenpos.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    boolean existsByOrderTableIdInAndOrderStatusIn(
        List<Long> orderTableIds,
        List<String> orderStatuses
    );
}
