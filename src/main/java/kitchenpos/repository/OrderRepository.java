package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdInAndOrderStatusIn(List<OrderTable> orderTables, List<String> orderStatus);

    boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<String> name);
}
