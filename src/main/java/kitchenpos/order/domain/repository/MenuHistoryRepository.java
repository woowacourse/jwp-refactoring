package kitchenpos.order.domain.repository;

import kitchenpos.order.domain.MenuHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface MenuHistoryRepository extends Repository<MenuHistory, Long> {

    MenuHistory save(final MenuHistory menuHistory);

    @Query("select id from MenuHistory where menuId = :menuId order by id desc")
    Optional<Long> findIdByMenuIdOrderByIdDesc(final Long menuId);
}
