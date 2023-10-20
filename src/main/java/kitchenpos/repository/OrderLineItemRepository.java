package kitchenpos.repository;

import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long>, OrderLineItemDao {
}
