package kitchenpos.support;

import kitchenpos.menu.application.dto.request.menu.MenuProductRequest;
import kitchenpos.menu.application.dto.request.menu.MenuRequest;
import kitchenpos.menu.application.dto.request.product.ProductRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;
import kitchenpos.order.application.dto.request.order.OrderLineItemRequest;
import kitchenpos.order.application.dto.request.order.OrderRequest;
import kitchenpos.order.domain.GuestCount;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderTable;

import java.math.BigDecimal;
import java.util.List;

public class Fixture {

    public static Menu makeMenu(final String name, final long price, final long menuGroupId,
                                final List<MenuProduct> menuProducts) {
        return new Menu(name, new Price(BigDecimal.valueOf(price)), menuGroupId, menuProducts);
    }

    public static Product makeProduct(final String name, final long price) {
        return new Product(name, new Price(BigDecimal.valueOf(price)));
    }

    public static List<OrderLineItem> makeSingleOrderLineItems() {
        return List.of(new OrderLineItem(1L, 10));
    }

    public static OrderTable makeEmptyOrderTable() {
        return new OrderTable(new GuestCount(0), true);
    }

    public static OrderTable makeNonEmptyOrderTable(final int numberOfGuests) {
        return new OrderTable(new GuestCount(numberOfGuests), false);
    }

    public static MenuRequest makeMenuRequest(final Price price, final long menuGroupId,
                                              final MenuProductRequest... menuProductRequests) {
        return new MenuRequest("메뉴", price.getValue(), menuGroupId, List.of(menuProductRequests));
    }

    public static MenuRequest makeMenuRequest(final long price, final long menuGroupId,
                                              final MenuProductRequest... menuProductRequests) {
        return new MenuRequest("메뉴", BigDecimal.valueOf(price), menuGroupId, List.of(menuProductRequests));
    }

    public static ProductRequest makeProductRequest(final String name, final long price) {
        return new ProductRequest(name, BigDecimal.valueOf(price));
    }

    public static OrderRequest makeOrderRequest(final Long orderTableId, final OrderLineItemRequest... orderLineItems) {
        return new OrderRequest(orderTableId, List.of(orderLineItems));
    }
}
