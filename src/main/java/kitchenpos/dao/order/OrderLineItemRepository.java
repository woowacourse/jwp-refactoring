package kitchenpos.dao.order;

import kitchenpos.domain.order.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
//    List<OrderLineItem> findByOrderId(Long id);
}
