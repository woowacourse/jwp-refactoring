package kitchenpos.application;

import kitchenpos.event.ValidateExistMenuEvent;
import kitchenpos.repository.MenuRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MenuEventHandler {

    private final MenuRepository menuRepository;

    public MenuEventHandler(final MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @EventListener
    @Transactional
    public void validateExistMenu(final ValidateExistMenuEvent event) {
        menuRepository.findById(event.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 메뉴입니다."));
    }
}
