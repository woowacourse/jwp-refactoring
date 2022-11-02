package kitchenpos.menu.eventListener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kitchenpos.event.ValidateExistMenusEvent;
import kitchenpos.menu.domain.MenuRepository;

@Component
public class MenuEventListener {

    private final MenuRepository menuRepository;

    public MenuEventListener(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @EventListener
    public void validateExistMenus(ValidateExistMenusEvent event) {
        long numberOfOrderedMenus = event.getMenuIds().size();
        long numberOfExistMenus = menuRepository.countByIdIn(event.getMenuIds());

        if (numberOfOrderedMenus != numberOfExistMenus) {
            throw new IllegalArgumentException("존재하지 않는 메뉴가 있습니다.");
        }
    }
}
