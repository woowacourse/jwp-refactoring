package kitchenpos.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kitchenpos.domain.OrderLineItem;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

    @Query("SELECT ori FROM OrderLineItem ori WHERE ori.order.id = (:orderId)")
    List<OrderLineItem> findAllByOrderId(@Param("orderId") Long orderId);
}
