package kitchenpos.order.domain;


import java.util.List;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableAndOrderStatusIn(OrderTable orderTable, List<OrderStatus> orderStatuses);

    boolean existsByOrderTableInAndOrderStatusIn(List<OrderTable> orderTables, List<OrderStatus> orderStatuses);
}
