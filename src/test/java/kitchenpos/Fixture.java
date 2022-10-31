package kitchenpos;

import kitchenpos.application.menu.dto.request.menu.MenuProductRequest;
import kitchenpos.application.menu.dto.request.menu.MenuRequest;
import kitchenpos.application.menu.dto.request.product.ProductRequest;
import kitchenpos.application.order.dto.request.order.OrderLineItemRequest;
import kitchenpos.application.order.dto.request.order.OrderRequest;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Price;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.order.GuestCount;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderTable;

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
