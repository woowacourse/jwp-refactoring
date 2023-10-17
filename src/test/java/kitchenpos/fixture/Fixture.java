package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Fixture {
    public static Menu menu(final String name, final int price, final Long menuGroupId, final List<MenuProduct> products){
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(new BigDecimal(price));
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(products);

        return menu;
    }

    public static MenuProduct menuProduct(final Long menuId, final Long productId, final Long quantity){
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    public static MenuGroup menuGroup(final String name){
        final MenuGroup menuGroup = new MenuGroup(name);
        return menuGroup;
    }

    public static Product product(final String name, final double price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(new BigDecimal(price));

        return product;
    }

    public static OrderLineItem orderLineItem(final Long orderId, final Long menuId, final Long quantity) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);

        return orderLineItem;
    }

    public static OrderTable orderTable(final Long tableGroupId, final int guestsNum, final Boolean isEmpty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(guestsNum);
        orderTable.setEmpty(isEmpty);

        return orderTable;
    }

    public static Order order(final Long id, final Long orderTableId, final LocalDateTime now, final List<OrderLineItem> orderLineItem) {
        final Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderedTime(now);
        order.setOrderLineItems(orderLineItem);

        return order;
    }

    public static TableGroup orderTableGroup(final LocalDateTime now, final List<OrderTable> orderTables) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(now);
        tableGroup.setOrderTables(orderTables);

        return tableGroup;
    }
}
