package kitchenpos.order.domain.repository;

import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTable_IdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatus);

    boolean existsByOrderTable_IdAndOrderStatusIn(Long orderTableId, List<String> list);
}
