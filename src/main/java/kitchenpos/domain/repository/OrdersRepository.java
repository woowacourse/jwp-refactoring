package kitchenpos.domain.repository;

import java.util.List;
import kitchenpos.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    boolean existsByOrderTableIdAndOrderStatusIn(
        Long orderTableId,
        List<String> orderStatuses
    );

    boolean existsByOrderTableIdInAndOrderStatusIn(
        List<Long> orderTableIds,
        List<String> orderStatuses
    );
}
