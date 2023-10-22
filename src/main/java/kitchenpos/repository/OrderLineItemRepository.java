package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

}
