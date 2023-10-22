package kitchenpos.domain.menu;

import java.util.List;
import java.util.Objects;
import kitchenpos.dao.menu.MenuRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MenuEventHandler {

    private final MenuRepository menuRepository;

    public MenuEventHandler(final MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @EventListener
    public void handle(final MenuExistValidationEvent event) {
        final List<Long> menuIds = event.getMenuIds();
        final List<Menu> menus = menuRepository.findAllByIdIn(menuIds);
        if (menus.isEmpty() || !Objects.equals(menus.size(), menuIds.size())) {
            throw new IllegalArgumentException("Menu does not exist.");
        }
    }
}
