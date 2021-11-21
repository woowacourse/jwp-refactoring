package kitchenpos.domain.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where o.orderTable.id in :orderTableIds")
    List<Order> findOrderByOrderTableIds(@Param("orderTableIds") List<Long> ids);

    @Query("select o from Order o where o.orderTable.id = :orderTableId")
    Optional<Order> findByOrderTableId(@Param("orderTableId") Long id);
}
