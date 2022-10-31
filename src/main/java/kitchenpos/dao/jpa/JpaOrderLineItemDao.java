package kitchenpos.dao.jpa;

import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderLineItemDao extends OrderLineItemDao, JpaRepository<OrderLineItem, Long> {
}
