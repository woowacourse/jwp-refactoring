package kitchenpos.order.domain.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where o.orderTableId in :orderTableIds")
    List<Order> findByOrderTableIds(@Param("orderTableIds") List<Long> ids);

    @Query("select o from Order o where o.orderTableId = :orderTableId")
    Optional<Order> findByOrderTableId(@Param("orderTableId") Long id);
}
