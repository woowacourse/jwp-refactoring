package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIsInAndOrderStatusIsIn(final List<OrderTable> orderTables,
                                                     final List<String> orderStatuses);

    boolean existsByOrderTableAndOrderStatusIsIn(final OrderTable orderTable, final List<String> orderStatuses);
}
