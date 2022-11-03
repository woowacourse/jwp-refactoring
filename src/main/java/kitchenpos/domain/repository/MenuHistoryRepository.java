package kitchenpos.domain.repository;

import java.util.Optional;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuHistory;
import org.springframework.data.repository.Repository;

public interface MenuHistoryRepository extends Repository<MenuHistory, Long> {

    MenuHistory save(MenuHistory entity);

    Optional<MenuHistory> findById(Long id);

    Optional<MenuHistory> findFirstByMenuOrderByCreatedTimeDesc(Menu menu);
}
