package kitchenpos.menu.eventHandler;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kitchenpos.event.CheckExistMenusEvent;
import kitchenpos.menu.domain.MenuRepository;

@Component
public class MenuEventHandler {

    private final MenuRepository menuRepository;

    public MenuEventHandler(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @EventListener
    public void validateExistMenus(CheckExistMenusEvent event) {
        long numberOfOrderedMenus = event.getMenuIds().size();
        long numberOfExistMenus = menuRepository.countByIdIn(event.getMenuIds());

        if (numberOfOrderedMenus != numberOfExistMenus) {
            throw new IllegalArgumentException("존재하지 않는 메뉴가 있습니다.");
        }
    }
}
