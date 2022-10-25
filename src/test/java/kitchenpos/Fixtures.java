package kitchenpos;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
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
import org.assertj.core.api.ListAssert;

@SuppressWarnings("NonAsciiCharacters")
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

    public static Menu 메뉴_치킨그룹(MenuProduct... menuProducts) {
        return new Menu(1L, "후라이드치킨", BigDecimal.valueOf(16000),
                메뉴그룹_한마리메뉴().getId(),
                Arrays.asList(menuProducts));
    }

    public static MenuProduct 메뉴상품_후라이드() {
        return new MenuProduct(1L, 1L, 상품_후라이드().getId(), 1);
    }


    public static OrderTable 테이블_1(long tableGroupId) {
        return new OrderTable(1L, tableGroupId, 0, false);
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

    public static OrderLineItem 주문아이템(long menuId) {
        return new OrderLineItem(1L, 1L, menuId, 1L);
    }

    public static OrderLineItem 주문아이템_후라이드() {
        return new OrderLineItem(1L, 1L, 상품_후라이드().getId(), 1L);
    }

    public static Order 주문_테이블1() {
        return new Order(1L, 테이블_1().getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                List.of(주문아이템_후라이드()));
    }

    public static Order 주문_테이블1(List<OrderLineItem> items) {
        return new Order(1L, 테이블_1().getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                items);
    }

    public static Order 주문_테이블1(OrderStatus status, List<OrderLineItem> items) {
        return new Order(1L, 테이블_1().getId(),
                status.name(),
                LocalDateTime.now(),
                items);
    }

    @SafeVarargs
    public static <ELEMENT> void 검증_필드비교_값포함(ListAssert<ELEMENT> assertThat, ELEMENT... values) {
        assertThat.usingRecursiveFieldByFieldElementComparator()
                .contains(values);
    }

    public static <ELEMENT> void 검증_필드비교_동일_목록(List<ELEMENT> list, List<ELEMENT> values, String... ignore) {
        for (int i = 0; i < list.size(); i++) {
            assertThat(list.get(i)).usingRecursiveComparison()
                    .ignoringFields(ignore)
                    .isEqualTo(values.get(i));
        }
    }

//    public static <ELEMENT> ListAssert<ELEMENT> 검증_필드비교_값포함(ListAssert<ELEMENT> assertThat, ELEMENT... values) {
//        return assertThat.usingRecursiveFieldByFieldElementComparator()
//                .contains(values);
//    }
}
