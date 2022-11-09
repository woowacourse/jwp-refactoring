package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.order.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

    List<OrderLineItem> findBySeqIn(final List<Long> ids);
}
