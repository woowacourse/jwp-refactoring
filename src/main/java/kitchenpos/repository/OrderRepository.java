package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatus);

    List<Order> findAllByTableGroup(OrderTable orderTable);
}
