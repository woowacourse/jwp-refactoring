package kitchenpos.domain.repository;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableInAndOrderStatusIn(List<OrderTable> orderTable, List<String> orderStatus);

    boolean existsByOrderTableAndOrderStatusIn(OrderTable orderTable, List<String> orderStatus);
}
