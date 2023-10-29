package kitchenpos.order;

import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuHistory;
import kitchenpos.menu.MenuRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuHistoryRecorder {

    private final MenuRepository menuRepository;

    public MenuHistoryRecorder(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public MenuHistory record(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴가 존재하지 않습니다."));

        return new MenuHistory(menuId, menu.getName(), menu.getPrice());
    }
}
