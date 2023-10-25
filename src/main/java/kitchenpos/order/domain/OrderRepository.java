package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.domain.vo.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderTable(OrderTable orderTable);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> list);
}
