package kitchenpos.application.menu;

import static kitchenpos.exception.menu.MenuExceptionType.MENU_NOT_FOUND;

import java.util.List;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.event.OrderedMenuEvent;
import kitchenpos.exception.menu.MenuException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderedMenuEventListener {

    private final MenuRepository menuRepository;

    public OrderedMenuEventListener(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @EventListener
    public void validateAllMenuIdExists(OrderedMenuEvent orderedEvent) {
        List<Long> menuIds = orderedEvent.menuIds();
        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new MenuException(MENU_NOT_FOUND);
        }
    }
}
