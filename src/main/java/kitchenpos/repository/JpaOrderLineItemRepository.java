package kitchenpos.repository;

import kitchenpos.domain.order.OrderLineItem;
import org.springframework.data.repository.Repository;

public interface JpaOrderLineItemRepository extends Repository<OrderLineItem, Long>, OrderLineItemRepository {
}
