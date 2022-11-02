package kitchenpos.menu.event;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;

@Component
public class MenuEventHandler {

    private MenuRepository menuRepository;

    public MenuEventHandler(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @EventListener
    public void verifiedMenus(VerifiedMenusEvent event) {
        final List<Menu> menus = menuRepository.findAllByIdIn(event.getMenuIds());

        if (event.getMenuIds().size() != menus.size()) {
            throw new IllegalArgumentException("존재하지 않는 메뉴입니다.");
        }
    }

}
