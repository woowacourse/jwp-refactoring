package kitchenpos.repository;

import kitchenpos.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
    @Query("SELECT oi FROM OrderLineItem oi JOIN FETCH Orders o WHERE o.id = :orderId")
    List<OrderLineItem> findAllByOrderId(Long orderId);
}
