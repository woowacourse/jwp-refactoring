package kitchenpos.order.domain.repository;

import kitchenpos.order.domain.MenuHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MenuHistoryRepository extends Repository<MenuHistory, Long> {

    MenuHistory save(final MenuHistory menuHistory);

    @Query(value = "select id from menu_history where menu_id = :menuId order by id desc limit 1", nativeQuery = true)
    Optional<Long> findTopIdByMenuIdOrderByIdDesc(@Param("menuId") final Long menuId);
}
