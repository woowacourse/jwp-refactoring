package kitchenpos.common.repository.order;

import java.util.List;
import kitchenpos.common.domain.order.OrderLineItem;
import org.springframework.data.repository.Repository;

public interface OrderLineItemRepository extends Repository<OrderLineItem, Long> {

    OrderLineItem save(OrderLineItem entity);

    List<OrderLineItem> findAll();

    List<OrderLineItem> findAllByOrderId(Long orderId);
}
