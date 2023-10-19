package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableInAndOrderStatusIn(final List<OrderTable> orderTables, final List<OrderStatus> orderStatuses);

    boolean existsByOrderTableAndOrderStatusIn(final Long orderTableId, final List<OrderStatus> orderStatuses);

}
