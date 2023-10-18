package kitchenpos.repository;

import kitchenpos.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

    @Query("select oli " +
           "from OrderLineItem oli " +
           "where oli.order.id = :orderId")
    List<OrderLineItem> findAllByOrderId(@Param("orderId") final Long orderId);
}
