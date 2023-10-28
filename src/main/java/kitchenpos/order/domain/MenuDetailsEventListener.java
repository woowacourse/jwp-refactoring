package kitchenpos.order.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MenuDetailsEventListener {

    private final MenuRepository menuRepository;

    public MenuDetailsEventListener(final MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @EventListener
    @Transactional
    public void takeSnapshot(final MenuDetailsSnapshotEvent event) {
        final Order order = event.getOrder();

        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();
        final List<Long> menuIds = extractMenuIds(orderLineItems);
        final List<Menu> menus = menuRepository.findAllById(menuIds);

        for (OrderLineItem orderLineItem : orderLineItems) {
            final Menu savedMenu = findRightMenu(menus, orderLineItem);
            final MenuDetails menuDetails = orderLineItem.getMenuDetails();
            menuDetails.updatePrice(savedMenu.getPrice());
            menuDetails.updateName(savedMenu.getName());
        }
    }

    private List<Long> extractMenuIds(final List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    private Menu findRightMenu(final List<Menu> menus, final OrderLineItem orderLineItem) {
        return menus.stream()
                .filter(each -> each.getId() == orderLineItem.getMenuId())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));
    }
}
