package kitchenpos;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface OrderLineItemRepository extends Repository<OrderLineItem, Long> {
    OrderLineItem save(OrderLineItem entity);

    Optional<OrderLineItem> findById(Long id);

    List<OrderLineItem> findAll();
}
