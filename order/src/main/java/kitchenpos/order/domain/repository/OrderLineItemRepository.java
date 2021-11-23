package kitchenpos.order.domain.repository;

import java.util.List;
import kitchenpos.order.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

    List<OrderLineItem> findAllByOrderId(Long orderId);
}
