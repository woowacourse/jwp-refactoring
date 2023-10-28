package kitchenpos.infrastructure.persistence;

import kitchenpos.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaOrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

    OrderLineItem save(final OrderLineItem entity);

    Optional<OrderLineItem> findById(final Long id);

    List<OrderLineItem> findAll();
}
