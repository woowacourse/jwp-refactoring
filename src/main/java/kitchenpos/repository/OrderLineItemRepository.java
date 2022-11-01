package kitchenpos.repository;

import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.OrderLineItem;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long>, OrderLineItemDao {
}
