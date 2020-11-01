package kitchenpos;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.OrderTableIdRequest;
import kitchenpos.dto.TableGroupRequest;

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
            .menu(menu)
            .build();
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
