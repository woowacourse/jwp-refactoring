package kitchenpos.domain.order.repository;

import java.util.List;
import kitchenpos.domain.order.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

    List<OrderLineItem> findAllByOrderId(Long orderId);
}
