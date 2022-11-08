package kitchenpos.order.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kitchenpos.order.domain.OrderMenu;

public interface JpaOrderMenuRepository extends JpaRepository<OrderMenu, Long> {

    @Query("select o from OrderMenu o where o.menuId = ?1 order by o.createdTime desc")
    Optional<OrderMenu> findLatestByMenuId(final Long menuId);
}
