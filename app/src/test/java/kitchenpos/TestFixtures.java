package kitchenpos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.menu.application.dto.MenuProductRequest;
import kitchenpos.menu.application.dto.MenuRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.application.dto.OrderLineItemRequest;
import kitchenpos.order.application.dto.OrderRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

public class TestFixtures {

    public static MenuGroup createMenuGroup() {
        return MenuGroup.builder()
                .id(1L)
                .name("메뉴그룹이름")
                .build();
    }

    public static MenuProduct createMenuProduct() {
        return MenuProduct.builder()
                .id(1L)
                .menuId(createMenu().getId())
                .productId(1L)
                .quantity(1L)
                .build();
    }

    public static MenuProduct createMenuProduct(Long id, Menu menu, Long quantity) {
        return MenuProduct.builder()
                .menuId(menu.getId())
                .productId(id)
                .quantity(quantity)
                .build();
    }

    public static Menu createMenu() {
        return Menu.builder()
                .id(1L)
                .name("메뉴이름")
                .price(BigDecimal.valueOf(1000))
                .menuGroupId(createMenuGroup().getId())
                .build();
    }

    public static Menu createMenu(Long id) {
        return Menu.builder()
                .id(id)
                .name("메뉴이름")
                .price(BigDecimal.valueOf(1000))
                .menuGroupId(createMenuGroup().getId())
                .build();
    }

    public static OrderLineItem createOrderLineItem(Long id) {
        return OrderLineItem.builder()
                .id(id)
                .order(createOrder().getId())
                .menuId(1L)
                .menuName("메뉴이름1")
                .menuPrice(BigDecimal.valueOf(1000L))
                .quantity(1L)
                .build();
    }

    public static Order createOrder() {
        return Order.builder()
                .id(1L)
                .orderTableId(1L)
                .orderStatus(OrderStatus.COMPLETION.name())
                .orderedTime(LocalDateTime.now())
                .build();
    }

    public static MenuRequest createMenuRequest(Menu menu) {
        final List<MenuProductRequest> menuProductRequests = Stream.of(createMenuProduct())
                .map(MenuProductRequest::new)
                .collect(Collectors.toList());
        return new MenuRequest(menu.getName(), menu.getPrice().longValue(), menu.getMenuGroupId(),
                menuProductRequests);
    }

    public static OrderRequest createOrderRequest(Order order) {
        order.updateOrderLineItems(Arrays.asList(createOrderLineItem(1L), createOrderLineItem(2L)));
        final List<OrderLineItemRequest> orderLineItemRequests = order.getOrderLineItems().stream()
                .map(OrderLineItemRequest::new)
                .collect(Collectors.toList());
        return new OrderRequest(order.getOrderTableId(), orderLineItemRequests);
    }
}
