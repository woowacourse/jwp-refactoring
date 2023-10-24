package kitchenpos.domain.order;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrderLineItemRepository extends CrudRepository<OrderLineItem, Long> {

    Optional<OrderLineItem> findByMenuId(Long menuId);
}
