package kitchenpos.menu.application;

import kitchenpos.common.event.ValidateExistMenuEvent;
import kitchenpos.menu.repository.MenuRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MenuEventHandler {

    private final MenuRepository menuRepository;

    public MenuEventHandler(final MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @EventListener
    private void validateExistMenu(final ValidateExistMenuEvent dto) {
        menuRepository.findById(dto.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 메뉴입니다."));
    }
}
