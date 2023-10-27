package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderLineItem;
import org.springframework.data.repository.Repository;

public interface OrderLineItemDao extends Repository<OrderLineItem, Long> {

    OrderLineItem save(OrderLineItem entity);

    Optional<OrderLineItem> findById(Long id);

    List<OrderLineItem> findAll();
}
