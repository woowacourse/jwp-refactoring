package kitchenpos.domain.repository;

import kitchenpos.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

    @Query("update OrderLineItem o set o.menu.id = :newId where o.menu.id = :originalId")
    void updateMenuIds(Long originalId, Long newId);
}
