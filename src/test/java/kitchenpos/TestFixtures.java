package kitchenpos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dtos.MenuProductRequest;
import kitchenpos.application.dtos.MenuRequest;
import kitchenpos.application.dtos.OrderLineItemRequest;
import kitchenpos.application.dtos.OrderRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Product;

public class TestFixtures {

    public static Product createProduct() {
        return Product.builder()
                .id(1L)
                .name("상품이름")
                .price(BigDecimal.valueOf(1000))
                .build();
    }

    public static MenuGroup createMenuGroup() {
        return MenuGroup.builder()
                .id(1L)
                .name("메뉴그룹이름")
                .build();
    }

    public static MenuProduct createMenuProduct() {
        return MenuProduct.builder()
                .id(1L)
                .menuId(1L)
                .productId(1L)
                .quantity(1L)
                .build();
    }

    public static Menu createMenu() {
        return Menu.builder()
                .id(1L)
                .name("메뉴이름")
                .price(BigDecimal.valueOf(1000))
                .menuGroupId(1L)
                .menuProducts(Collections.singletonList(createMenuProduct()))
                .build();
    }

    public static OrderLineItem createOrderLineItem(Long id) {
        return OrderLineItem.builder()
                .id(id)
                .orderId(1L)
                .menuId(1L)
                .quantity(1L)
                .build();
    }

    public static Order createOrder() {
        return Order.builder()
                .id(1L)
                .orderTableId(1L)
                .orderStatus(OrderStatus.COMPLETION.name())
                .orderedTime(LocalDateTime.now())
                .orderLineItems(Arrays.asList(createOrderLineItem(1L), createOrderLineItem(2L)))
                .build();
    }

    public static MenuRequest createMenuRequest(Menu menu) {
        final List<MenuProductRequest> menuProductRequests = menu.getMenuProducts().stream()
                .map(MenuProductRequest::new)
                .collect(Collectors.toList());
        return new MenuRequest(menu.getName(), menu.getPrice().longValue(), menu.getMenuGroupId(), menuProductRequests);
    }

    public static OrderRequest createOrderRequest(Order order) {
        final List<OrderLineItemRequest> orderLineItemRequests = order.getOrderLineItems().stream()
                .map(OrderLineItemRequest::new)
                .collect(Collectors.toList());
        return new OrderRequest(order.getOrderTableId(), orderLineItemRequests);
    }
}
