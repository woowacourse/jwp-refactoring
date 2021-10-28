package kitchenpos.order.repository;

import java.util.List;
import kitchenpos.order.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
    List<OrderLineItem> findByOrder_id(Long orderId);
}
