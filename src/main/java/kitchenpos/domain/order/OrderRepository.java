package kitchenpos.domain.order;

import kitchenpos.domain.table.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderTableAndOrderStatusIn(final OrderTable orderTable, final List<String> list);

    boolean existsByOrderTableInAndOrderStatusIn(final List<OrderTable> orderTables, final List<String> status);
}
