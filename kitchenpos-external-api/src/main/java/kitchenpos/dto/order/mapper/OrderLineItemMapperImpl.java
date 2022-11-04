package kitchenpos.dto.order.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderedMenu;
import kitchenpos.dto.order.request.OrderLineItemCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderLineItemMapperImpl implements OrderLineItemMapper {

    @Override
    public List<OrderLineItem> toOrderLineItems(final List<OrderLineItemCreateRequest> orderLineItemCreateRequests,
                                                final List<Menu> menus) {
        return orderLineItemCreateRequests.stream()
                .map(request -> toOrderLineItem(request, menus))
                .collect(Collectors.toList());
    }

    private OrderLineItem toOrderLineItem(final OrderLineItemCreateRequest orderLineItemCreateRequest,
                                          final List<Menu> menus) {
        OrderedMenu orderedMenu = menus.stream()
                .filter(menu -> menu.getId().equals(orderLineItemCreateRequest.getMenuId()))
                .map(menu -> new OrderedMenu(menu.getName(), menu.getPrice()))
                .findAny()
                .orElseThrow();
        return new OrderLineItem(orderLineItemCreateRequest.getQuantity(), orderedMenu);
    }
}
