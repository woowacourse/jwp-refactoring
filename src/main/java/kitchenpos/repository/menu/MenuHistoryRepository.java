package kitchenpos.repository.menu;

import java.util.Optional;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuHistory;
import org.springframework.data.repository.Repository;

public interface MenuHistoryRepository extends Repository<MenuHistory, Long> {

    MenuHistory save(MenuHistory entity);

    Optional<MenuHistory> findFirstByMenuOrderByCreatedTimeDesc(Menu menu);

    default MenuHistory findMostRecentByMenu(Menu menu) {
        return findFirstByMenuOrderByCreatedTimeDesc(menu).orElseThrow(
                () -> new IllegalArgumentException("메뉴 정보가 존재하지 않습니다."));
    }
}
