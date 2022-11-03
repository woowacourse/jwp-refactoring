package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTest {

    @DisplayName("주문 라인 항목이 없으면 예외를 발생시킨다.")
    @Test
    void notExistOrderLineItem_exception() {
        // then
        assertThatThrownBy(() -> new Order(new OrderTable(2, false), OrderStatus.COOKING.name(),
                LocalDateTime.now(), List.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 라인 항목의 메뉴는 중복이 있으면 안된다.")
    @Test
    void distinctOrderLineItem_exception() {
        // given
        Menu 메뉴1 = new Menu("메뉴1", new Price(1000L), new MenuGroup("메뉴 그룹"), List.of(
                new MenuProduct(null, new Product("상품", 1000L), 2)
        ));
        Menu 메뉴2 = new Menu("메뉴2", new Price(1000L), new MenuGroup("메뉴 그룹"), List.of(
                new MenuProduct(null, new Product("상품", 1000L), 2)
        ));

        List<OrderLineItem> orderLineItems = List.of(
                new OrderLineItem(메뉴1, 2),
                new OrderLineItem(메뉴1, 2),
                new OrderLineItem(메뉴2, 2)
        );

        // then
        assertThatThrownBy(
                () -> new Order(new OrderTable(2, false), OrderStatus.COOKING.name(),
                        LocalDateTime.now(), orderLineItems)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
