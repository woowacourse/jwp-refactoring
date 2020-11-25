package kitchenpos.repository;

import java.util.List;
import java.util.Set;
import kitchenpos.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
    List<OrderLineItem> findAllByOrderId(Long orderId);

    List<OrderLineItem> findAllByOrderIdIn(Set<Long> orderIds);
}
