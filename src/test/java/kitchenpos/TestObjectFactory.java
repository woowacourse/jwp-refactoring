package kitchenpos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
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

    public static Order createOrder(OrderTable table) {
        return Order.builder()
            .orderTable(table)
            .orderStatus(OrderStatus.COOKING.name())
            .orderedTime(LocalDateTime.now())
            .build();
    }

    public static MenuGroup createMenuGroup(String name) {
        return MenuGroup.builder()
            .name(name)
            .build();
    }

    public static Product createProduct(int price) {
        return Product.builder()
            .name("강정치킨")
            .price(BigDecimal.valueOf(price))
            .build();
    }

    public static OrderTable createTable(boolean empty) {
        return OrderTable.builder()
            .numberOfGuests(0)
            .empty(empty)
            .build();
    }
}
