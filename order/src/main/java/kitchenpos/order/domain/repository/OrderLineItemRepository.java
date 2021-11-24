package kitchenpos.order.domain.repository;

import kitchenpos.order.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

    @Query("update OrderLineItem o set o.menuId = :newId where o.menuId = :originalId")
    void updateMenuIds(Long originalId, Long newId);

    boolean existsByMenuId(Long menuId);
}
