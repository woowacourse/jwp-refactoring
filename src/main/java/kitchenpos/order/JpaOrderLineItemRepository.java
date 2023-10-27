package kitchenpos.order;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
    List<OrderLineItem> findAllByOrderId(final Long orderId);

}
