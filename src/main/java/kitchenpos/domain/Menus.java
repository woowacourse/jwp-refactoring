package kitchenpos.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import kitchenpos.exception.KitchenException;
import org.springframework.util.CollectionUtils;

public class Menus {

    private final List<Menu> menus;

    public Menus(List<Menu> menus) {
        this.menus = menus;
    }

    public List<OrderLineItem> convertToOrderLineItem(OrderCreateRequest request) {
        List<OrderLineItemCreateRequest> orderLineItems = request.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new KitchenException("주문 항목이 비어있습니다.");
        }

        return orderLineItems.stream()
            .map(orderLineItemCreateRequest -> {
                Menu menu = findMenu(menus, orderLineItemCreateRequest.getMenuId());
                Long quantity = orderLineItemCreateRequest.getQuantity();
                return new OrderLineItem(menu, quantity);
            }).collect(Collectors.toList());
    }

    private Menu findMenu(List<Menu> menus, Long menuId) {
        return menus.stream()
            .filter(menu -> menuId.equals(menu.getId()))
            .findAny()
            .orElseThrow(() -> new KitchenException("존재하지 않는 메뉴가 포함되어 있습니다."));
    }
}
