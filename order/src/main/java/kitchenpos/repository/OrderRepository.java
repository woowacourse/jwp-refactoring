package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where o.orderTableId = :orderTableId")
    List<Order> findByOrderTableId(Long orderTableId);

    @Query("select o from Order o where o.orderTableId in :orderTableIds")
    List<Order> findByOrderTableIdIn(List<Long> orderTableIds);
}
