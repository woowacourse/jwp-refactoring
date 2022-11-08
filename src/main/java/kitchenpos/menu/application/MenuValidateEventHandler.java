package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.dto.OrderLineItemsValidateEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MenuValidateEventHandler {
    private final MenuDao menuDao;

    public MenuValidateEventHandler(final MenuDao menuDao) {
        this.menuDao = menuDao;
    }

    @EventListener
    public void validateOrderMenuIds(final OrderLineItemsValidateEvent event) {
        final List<Long> orderLineItemsMenuIds = event.getOrderLineItemsMenuIds();
        validateIsNotEmpty(orderLineItemsMenuIds);
        validateExistMenuIds(orderLineItemsMenuIds);
    }

    private void validateIsNotEmpty(final List<Long> orderLineItemsMenuIds) {
        if (orderLineItemsMenuIds.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateExistMenuIds(final List<Long> orderLineItemsMenuIds) {
        final List<Menu> menus = menuDao.findAll();

        final List<Long> menuIds = menus.stream()
                .map(Menu::getId)
                .collect(Collectors.toList());

        if (!menuIds.containsAll(orderLineItemsMenuIds)) {
            throw new IllegalArgumentException();
        }
    }
}
