package kitchenpos.dao.order;

import java.util.List;
import kitchenpos.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
//    List<OrderLineItem> findByOrderId(Long id);
}
