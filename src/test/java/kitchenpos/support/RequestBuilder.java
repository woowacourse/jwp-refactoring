package kitchenpos.support;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

public class RequestBuilder {

    private static final String DEFAULT_PRODUCT_NAME = "콜라";
    private static final Integer DEFAULT_PRODUCT_PRICE = 2000;
    private static final String DEFAULT_MENU_GROUP_NAME = "음료 메뉴";
    private static final String DEFAULT_MENU_NAME = "포키 정식";

    public static Product ofProduct() {
        return ofProduct(DEFAULT_PRODUCT_PRICE);
    }

    public static Product ofProduct(final int price) {
        final Product product = new Product();
        product.setName(DEFAULT_PRODUCT_NAME);
        product.setPrice(new BigDecimal(price));

        return product;
    }

    public static MenuGroup ofMenuGroup() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(DEFAULT_MENU_GROUP_NAME);

        return menuGroup;
    }

    public static Menu ofMenu(final MenuGroup menuGroup, final List<Product> products, final int price) {
        final List<MenuProduct> menuProducts = products.stream()
                .map(RequestBuilder::ofMenuProduct)
                .collect(Collectors.toList());

        final Menu menu = new Menu();
        menu.setName(DEFAULT_MENU_NAME);
        menu.setPrice(new BigDecimal(price));
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(menuProducts);

        return menu;
    }

    public static OrderTable ofEmptyTable() {
        return ofTable(0, true);
    }

    public static OrderTable ofFullTable() {
        return ofTableWithGuests(2);
    }

    public static OrderTable ofTableWithGuests(final int numberOfGuests) {
        return ofTable(numberOfGuests, false);
    }

    public static TableGroup ofTableGroup(final OrderTable... orderTables) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(orderTables));

        return tableGroup;
    }

    public static Order ofOrder(final OrderStatus orderStatus) {
        final Order order = new Order();
        order.setOrderStatus(orderStatus.name());

        return order;
    }

    public static Order ofOrder(final Menu menu, final OrderTable orderTable) {
        return ofOrder(menu.getId(), orderTable.getId());
    }

    public static Order ofOrderWithoutMenu(final OrderTable orderTable) {
        return ofOrder(null, orderTable.getId());
    }

    private static OrderTable ofTable(final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    private static MenuProduct ofMenuProduct(final Product product) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(1);

        return menuProduct;
    }

    private static Order ofOrder(final Long menuId, final Long tableId) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(1);
        final List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);

        final Order order = new Order();
        order.setOrderTableId(tableId);
        order.setOrderLineItems(orderLineItems);

        return order;
    }
}
