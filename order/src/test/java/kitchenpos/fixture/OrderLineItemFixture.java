package kitchenpos.fixture;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderMenu;
import kitchenpos.domain.order.OrderMenuProduct;

public class OrderLineItemFixture {

    public static OrderLineItem 주문_항목(Menu menu, long quantity) {
        return new OrderLineItem(null, menu.getId(), quantity, toOrderMenu(menu));
    }

    public static OrderLineItem 주문_항목(Long menuId, long quantity) {
        return new OrderLineItem(null, menuId, quantity);
    }

    private static OrderMenu toOrderMenu(Menu menu) {
        return new OrderMenu(menu.getName(), menu.getPrice(), toOrderMenuProducts(menu.getMenuProducts()));
    }

    private static List<OrderMenuProduct> toOrderMenuProducts(MenuProducts menuProducts) {
        return menuProducts.getItems().stream()
                .map(item -> new OrderMenuProduct(item.getName(), item.getPrice(), item.getQuantity()))
                .collect(toList());
    }
}
