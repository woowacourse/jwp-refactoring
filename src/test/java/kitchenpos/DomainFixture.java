package kitchenpos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

public class DomainFixture {

    public static Product 후라이드치킨 = createProduct("후라이드치킨", 8_000);
    public static Product 양념치킨 = createProduct("양념치킨", 8_000);
    public static Product 피자 = createProduct("피자", 10_000);

    public static MenuGroup 메뉴그룹1 = createMenuGroup("메뉴그룹1");
    public static MenuGroup 메뉴그룹2 = createMenuGroup("메뉴그룹2");

    private static final int ONE_QUANTITY = 1;

    public static Product createProduct(final String name, final int price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));

        return product;
    }

    public static MenuGroup createMenuGroup(final String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }

    public static Menu createMenu(final String name,
                                  final int price,
                                  final MenuGroup menuGroup,
                                  final Product... products) {
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroup.getId());
        setMenuProducts(menu, products);
        return menu;
    }

    private static void setMenuProducts(final Menu menu, final Product[] products) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (final Product product : products) {
            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(product.getId());
            menuProduct.setQuantity(ONE_QUANTITY);
            menuProducts.add(menuProduct);
        }
        menu.setMenuProducts(menuProducts);
    }

    public static TableGroup createTableGroup(final OrderTable... orderTables) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(orderTables));
        return tableGroup;
    }

    public static OrderTable createOrderTable(final int numberOfGuests, final boolean isEmpty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(isEmpty);
        return orderTable;
    }

    public static OrderTable forUpdateEmpty(final boolean isEmpty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(isEmpty);
        return orderTable;
    }

    public static OrderTable forUpdateGuestNumber(final int numberOfGuests) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    public static Order createOrder(final OrderTable orderTable, final Menu... menus) {
        final Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        setOrderLineItems(order, menus);
        return order;
    }

    private static void setOrderLineItems(final Order order, final Menu[] menus) {
        final ArrayList<OrderLineItem> orderLineItems = new ArrayList<>();
        for (final Menu menu : menus) {
            final OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(menu.getId());
            orderLineItem.setQuantity(ONE_QUANTITY);
            orderLineItems.add(orderLineItem);
        }
        order.setOrderLineItems(orderLineItems);
    }

    public static Order forUpdateStatus(final OrderStatus orderStatus) {
        final Order order = new Order();
        order.setOrderStatus(orderStatus.name());
        return order;
    }
}
