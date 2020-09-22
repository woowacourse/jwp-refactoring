package kitchenpos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

public class TestObjectFactory {

    public static Order createOrder(OrderTable table, List<OrderLineItem> orderLineItems) {
        return Order.builder()
            .orderTableId(table.getId())
            .orderStatus(OrderStatus.COOKING.name())
            .orderLineItems(orderLineItems)
            .orderedTime(LocalDateTime.now())
            .build();
    }

    public static Order createOrder(OrderTable table) {
        return Order.builder()
            .orderTableId(table.getId())
            .orderStatus(OrderStatus.COOKING.name())
            .orderedTime(LocalDateTime.now())
            .build();
    }

    public static OrderLineItem createOrderLineItem(Menu menu) {
        return OrderLineItem.builder()
            .menuId(menu.getId())
            .quantity(2)
            .build();
    }

    public static Menu createMenu(int price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return Menu.builder()
            .name("강정치킨")
            .price(BigDecimal.valueOf(price))
            .menuGroupId(menuGroup.getId())
            .menuProducts(menuProducts)
            .build();
    }

    public static MenuGroup createMenuGroup() {
        return MenuGroup.builder()
            .name("강정메뉴")
            .build();
    }

    public static MenuProduct createMenuProduct(Product product) {
        return MenuProduct.builder()
            .productId(product.getId())
            .quantity(1)
            .build();
    }

    public static Product createProduct(int price) {
        return Product.builder()
            .name("강정치킨")
            .price(BigDecimal.valueOf(price))
            .build();
    }

    public static TableGroup createTableGroup(List<OrderTable> tables) {
        return TableGroup.builder()
            .orderTables(tables)
            .createdDate(LocalDateTime.now())
            .build();
    }

    public static OrderTable createTable(boolean empty) {
        return OrderTable.builder()
            .numberOfGuests(0)
            .empty(empty)
            .build();
    }
}
