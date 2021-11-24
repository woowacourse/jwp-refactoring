package kitchenpos.domain.repository;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    boolean existsByOrderTableInAndOrderStatusIn(
        List<OrderTable> orderTables,
        List<String> orderStatuses
    );
}
