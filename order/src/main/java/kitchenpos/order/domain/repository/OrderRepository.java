package kitchenpos.order.domain.repository;

import java.util.List;
import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where o.orderTableId in :orderTableIds")
    List<Order> findByOrderTableIds(List<Long> orderTableIds);

    @Query("select o from Order o where o.orderTableId = :orderTableId")
    List<Order> findByOrderTableId(Long orderTableId);
}
