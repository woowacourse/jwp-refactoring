package kitchenpos.domain.order.repository;

import kitchenpos.domain.order.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
    @Query("select ol from OrderLineItem ol where ol.menu.id = :menuId")
    List<OrderLineItem> findAllByMenuId(@Param("menuId") Long menuId);
}
