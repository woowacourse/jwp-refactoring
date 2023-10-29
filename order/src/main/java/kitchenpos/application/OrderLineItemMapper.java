package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderLineItemQuantityDto;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.vo.OrderLineItems;
import org.springframework.stereotype.Component;

@Component
public class OrderLineItemMapper {

    private final MenuRepository menuRepository;

    public OrderLineItemMapper(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public OrderLineItems toOrderLineItems(List<OrderLineItemQuantityDto> orderLineItemQuantities) {
        List<OrderLineItem> orderLineItems = convertToOrderLineItems(orderLineItemQuantities);
        return new OrderLineItems(orderLineItems);
    }

    private List<OrderLineItem> convertToOrderLineItems(List<OrderLineItemQuantityDto> orderLineItemQuantities) {
        return orderLineItemQuantities.stream()
                .map(this::createOrderLineItem)
                .collect(Collectors.toList());
    }

    private OrderLineItem createOrderLineItem(OrderLineItemQuantityDto orderLineItemQuantity) {
        Menu menu = getMenu(orderLineItemQuantity);
        return new OrderLineItem(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                orderLineItemQuantity.getQuantity()
        );
    }

    private Menu getMenu(OrderLineItemQuantityDto orderLineItemQuantity) {
        return menuRepository.findById(orderLineItemQuantity.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 메뉴입니다."));
    }
}
