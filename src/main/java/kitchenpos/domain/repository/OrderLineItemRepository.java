package kitchenpos.domain.repository;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

    //List<OrderLineItem> findAllByOrder(List<Order> orders);

    @Query("SELECT olt FROM OrderLineItem olt WHERE olt.order.id = :orderId")
    List<OrderLineItem> findAllByOrderId(@Param("orderId") Long orderId);
}
