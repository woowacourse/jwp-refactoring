package kitchenpos.utils;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class TestObjectUtils {

    public static final long NOT_EXIST_VALUE = -1;

    public static Menu createMenu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);

        return menu;
    }

    public static MenuProduct createMenuProduct(Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    public static Order createOrder(Long orderTableId, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);

        return order;
    }

    public static OrderLineItem createOrderLineItem(Long menuId, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);

        return orderLineItem;
    }
}
