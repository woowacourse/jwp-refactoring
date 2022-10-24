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

public class Fixtures {

    public static MenuGroup 메뉴그룹_한마리메뉴() {
        return new MenuGroup(1L, "한마리메뉴");
    }

    public static Product 상품_후라이드() {
        return new Product(1L, "후라이드", BigDecimal.valueOf(16000));
    }

    public static Menu 메뉴_후라이드치킨() {
        return new Menu(1L, "후라이드치킨", BigDecimal.valueOf(16000),
                메뉴그룹_한마리메뉴().getId(),
                List.of(메뉴상품_후라이드()));
    }

    public static MenuProduct 메뉴상품_후라이드() {
        return new MenuProduct(1L, 1L, 상품_후라이드().getId(), 1);
    }

    public static OrderTable 테이블_1() {
        return new OrderTable(1L, null, 0, false);
    }

    public static OrderTable 빈테이블_1() {
        return new OrderTable(1L, null, 0, true);
    }

    public static OrderTable 빈테이블_2() {
        return new OrderTable(2L, null, 0, true);
    }

    public static TableGroup 테이블그룹(List<OrderTable> tables) {
        return new TableGroup(1L, LocalDateTime.now(), tables);
    }

    public static TableGroup 테이블그룹2(List<OrderTable> tables) {
        return new TableGroup(2L, LocalDateTime.now(), tables);
    }

    public static OrderLineItem 주문아이템_후라이드() {
        return new OrderLineItem(1L, 1L, 상품_후라이드().getId(), 1L);
    }

    public static Order 주문_테이블1_후라이드() {
        return new Order(1L, 테이블_1().getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                List.of(주문아이템_후라이드()));
    }

    public static Order 주문_테이블1_후라이드(List<OrderLineItem> items) {
        return new Order(1L, 테이블_1().getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                items);
    }
}
