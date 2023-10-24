package kitchenpos.repository;

import kitchenpos.domain.OrderLineItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineItemRepository extends CrudRepository<OrderLineItem, Long> {

}
