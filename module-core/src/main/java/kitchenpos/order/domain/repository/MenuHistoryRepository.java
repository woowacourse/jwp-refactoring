package kitchenpos.order.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import kitchenpos.order.domain.MenuHistory;

public interface MenuHistoryRepository extends Repository<MenuHistory, Long> {

    MenuHistory save(final MenuHistory menuHistory);

    @Query(value = "select id from menu_history where menu_id = :menuId order by id desc limit 1", nativeQuery = true)
    Optional<Long> findTopIdByMenuIdOrderByIdDesc(@Param("menuId") final Long menuId);
}
