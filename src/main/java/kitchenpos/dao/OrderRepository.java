package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatus);

    boolean existsByOrderTableAndOrderStatusIn(Long orderTableId, List<String> orderStatus);
}
