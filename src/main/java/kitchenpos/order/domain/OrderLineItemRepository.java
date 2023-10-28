package kitchenpos.order.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface OrderLineItemRepository extends Repository<OrderLineItem, Long> {
    OrderLineItem save(OrderLineItem entity);

    Optional<OrderLineItem> findById(Long id);

    List<OrderLineItem> findAll();

    List<OrderLineItem> findAllByOrderId(Long orderId);
}
