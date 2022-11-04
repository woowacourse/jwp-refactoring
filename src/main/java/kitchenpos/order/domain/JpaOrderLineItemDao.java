package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderLineItemDao extends JpaRepository<OrderLineItem, Long>, OrderLineItemDao {
}
