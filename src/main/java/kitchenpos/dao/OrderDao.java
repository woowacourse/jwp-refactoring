package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDao extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdInAndOrderStatusIn(List<OrderTable> orderTables, List<OrderStatus> orderStatus);

    boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<OrderStatus> orderStatuses);
}
