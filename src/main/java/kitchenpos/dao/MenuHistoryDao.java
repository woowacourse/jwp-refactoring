package kitchenpos.dao;

import kitchenpos.domain.history.MenuHistory;

import java.time.LocalDateTime;
import java.util.List;

public interface MenuHistoryDao {

    MenuHistory save(MenuHistory entity);

    List<MenuHistory> findAllByDateAndMenuId(Long menuId, LocalDateTime searchTime);
}
