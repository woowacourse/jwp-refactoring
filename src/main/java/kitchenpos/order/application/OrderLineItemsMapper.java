package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.dto.OrderCreateRequest.OrderLineItemCreateRequest;
import kitchenpos.order.domain.OrderLineItem;
import org.springframework.stereotype.Component;

@Component
public class OrderLineItemsMapper {

    private final MenuRepository menuRepository;

    public OrderLineItemsMapper(final MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<OrderLineItem> mapFrom(final List<OrderLineItemCreateRequest> requestOrderLineItems) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (final OrderLineItemCreateRequest requestOrderLineItem : requestOrderLineItems) {
            final Menu menu = findMenuById(requestOrderLineItem);
            final OrderLineItem orderLineItem = new OrderLineItem(
                    requestOrderLineItem.getMenuId(),
                    requestOrderLineItem.getQuantity(),
                    menu.getName(),
                    new Price(menu.getPrice())
            );
            orderLineItems.add(orderLineItem);
        }
        return orderLineItems;
    }

    private Menu findMenuById(final OrderLineItemCreateRequest requestOrderLineItem) {
        return menuRepository.findById(requestOrderLineItem.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "존재하지 않는 메뉴입니다. id = " + requestOrderLineItem.getMenuId()));
    }
}
