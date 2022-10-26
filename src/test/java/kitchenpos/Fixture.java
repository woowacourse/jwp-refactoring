package kitchenpos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

@SuppressWarnings("NonAsciiCharacters")
public class Fixture {

    public static Product 상품의_가격은(BigDecimal price) {
        return new Product("맥도날드 페페로니 피자 버거", price);
    }

    public static MenuGroup 메뉴집합() {
        return new MenuGroup("햄버거");
    }

    public static MenuProduct 메뉴의_상품은(Product product) {
        return new MenuProduct(product.getId(), 1);
    }

    public static Menu 메뉴(MenuGroup menuGroup, MenuProduct... menuProducts) {
        return new Menu("햄버억", new BigDecimal(200_000), menuGroup.getId(), List.of(menuProducts));
    }

    public static OrderTable 테이블(boolean empty) {
        return new OrderTable(1, empty);
    }

    public static OrderLineItem 주문정보(Long menuId) {
        return new OrderLineItem(menuId, 1);
    }

    public static Order 주문(Long tableId, Long menuId) {
        return new Order("COOKING", tableId, LocalDateTime.now(), List.of(주문정보(menuId)));
    }

    public static TableGroup 테이블집합(OrderTable... orderTables) {
        return new TableGroup(LocalDateTime.now(), List.of(orderTables));
    }
}
