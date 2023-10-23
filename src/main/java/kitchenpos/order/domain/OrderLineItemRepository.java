package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.order.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

    @Query("SELECT oli FROM OrderLineItem oli WHERE oli.order.id = :orderId")
    List<OrderLineItem> findAllByOrderId(Long orderId);
}
