package kitchenpos;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu_group.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.table_group.domain.TableGroup;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.order_table.dto.OrderTableIdRequest;
import kitchenpos.table_group.dto.TableGroupRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class TestObjectFactory {

    public static OrderTableIdRequest createOrderTableIdRequest(Long id) {
        return OrderTableIdRequest.builder()
            .id(id)
            .build();
    }

    public static TableGroupRequest createTableGroupRequest(List<OrderTableIdRequest> tables) {
        return TableGroupRequest.builder()
            .orderTables(tables)
            .build();
    }

    public static MenuRequest createMenuRequest(int price, MenuGroup menuGroup,
        List<MenuProductRequest> menuProducts) {
        return MenuRequest.builder()
            .name("강정치킨")
            .price(BigDecimal.valueOf(price))
            .menuGroupId(menuGroup.getId())
            .menuProducts(menuProducts)
            .build();
    }

    public static MenuProductRequest createMenuProductRequest(Product product) {
        return MenuProductRequest.builder()
            .productId(product.getId())
            .quantity(2)
            .build();
    }

    public static OrderLineItem createOrderLineItem(Menu menu) {
        return OrderLineItem.builder()
            .menuId(menu.getId())
            .build();
    }

    public static OrderLineItem createOrderLineItem() {
        return createOrderLineItem(new Menu());
    }

    public static Order createOrder(OrderTable table, List<OrderLineItem> orderLineItems) {
        return Order.builder()
            .orderTable(table)
            .orderLineItems(orderLineItems)
            .build();
    }

    public static MenuGroup createMenuGroup(String name) {
        return MenuGroup.builder()
            .name(name)
            .build();
    }

    public static Menu createMenu(List<MenuProduct> menuProducts, int price) {
        return Menu.builder()
            .name("강정치킨")
            .price(BigDecimal.valueOf(price))
            .menuProducts(menuProducts)
            .build();
    }

    public static Product createProduct(int price) {
        return Product.builder()
            .name("강정치킨")
            .price(BigDecimal.valueOf(price))
            .build();
    }

    public static MenuProduct createMenuProduct(Product product, int quantity) {
        return MenuProduct.builder()
            .product(product)
            .quantity(quantity)
            .build();
    }

    public static OrderTable createOrderTable(boolean empty) {
        return OrderTable.builder()
            .numberOfGuests(0)
            .empty(empty)
            .build();
    }

    public static TableGroup createTableGroup(List<OrderTable> orderTables) {
        return TableGroup.builder()
            .orderTables(orderTables)
            .build();
    }
}
