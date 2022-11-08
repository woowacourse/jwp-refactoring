package kitchenpos.order.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderLineItemCreator {

    private final MenuRepository menuRepository;

    public OrderLineItemCreator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<OrderLineItem> create(final OrderRequest request) {
        final Map<Long, Menu> menus = findMenus(request);

        return request.getOrderLineItems().stream()
                .map(i -> createOrderLineItem(menus.get(i.getMenuId()), i.getQuantity()))
                .collect(Collectors.toList());
    }

    private Map<Long, Menu> findMenus(final OrderRequest request) {
        final List<Long> menuIds = request.getOrderLineItems().stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        final Map<Long, Menu> menus = menuRepository.findAllByIdIn(menuIds).stream()
                .collect(Collectors.toMap(Menu::getId, menu -> menu));

        if (menus.size() != menuIds.size()) {
            throw new IllegalArgumentException();
        }

        return menus;
    }

    private OrderLineItem createOrderLineItem(final Menu menu, final long quantity) {
        return new OrderLineItem(menu.getId(), menu.getName(), menu.getPrice(), quantity);
    }
}
