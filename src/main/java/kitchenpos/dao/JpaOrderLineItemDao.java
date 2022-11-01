package kitchenpos.dao;

import kitchenpos.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderLineItemDao extends JpaRepository<OrderLineItem, Long>, OrderLineItemDao {
}
