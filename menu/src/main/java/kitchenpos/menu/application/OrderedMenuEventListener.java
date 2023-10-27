package kitchenpos.menu.application;

import static kitchenpos.menu.exception.MenuExceptionType.MENU_NOT_FOUND;

import java.util.List;
import kitchenpos.event.OrderedMenuEvent;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.exception.MenuException;
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
