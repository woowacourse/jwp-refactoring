package kitchenpos.domain.order.repository;

import kitchenpos.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "select o from Order o " +
            "where o.orderTable.id = :orderTableId")
    Optional<Order> findByOrderTableId(@Param("orderTableId") Long orderTableId);

    @Query(value = "select o from Order o " +
            "where o.orderTable.id in :orderTableIds")
    List<Order> findAllByOrderTableIds(@Param("orderTableIds") List<Long> orderTableIds);
}
