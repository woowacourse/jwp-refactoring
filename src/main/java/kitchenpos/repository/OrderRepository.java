package kitchenpos.repository;

import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o JOIN o.orderLineItems")
    List<Order> joinAllOrderLineItems();

    @Query(value = "SELECT EXISTS " +
            "(SELECT 1 FROM orders o " +
            "WHERE o.order_table_id = :orderTableId " +
            "AND o.order_status = 'COMPLETION')", nativeQuery = true)
    boolean existsByOrderTableIdAndCompletion(@Param("orderTableId") final Long orderTableId);

    @Query("SELECT COUNT(1) " +
            "FROM Order o " +
            "WHERE o.orderTable.id IN :orderTableIds " +
            "AND o.orderStatus = 'COMPLETION'")
    Long countCompletionOrderByOrderTableIds(@Param("orderTableIds") final List<Long> orderTableIds);

    @Query("SELECT o FROM Order o JOIN o.orderLineItems WHERE o.id = :id")
    Optional<Order> joinOrderLineItemsById(@Param("id") final Long id);

    default Order joinOrderLineItemsMandatoryById(final Long id) {
        return joinOrderLineItemsById(id).orElseThrow(IllegalArgumentException::new);
    }
}
