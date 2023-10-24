package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.entity.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

    @Query("SELECT orderLineItem "
            + "FROM OrderLineItem orderLineItem "
            + "WHERE orderLineItem.menuId = :id")
    List<OrderLineItem> findAllByOrderId(final Long id);
}
