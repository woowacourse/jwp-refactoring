package kitchenpos.domain;

import kitchenpos.domain.vo.Name;
import kitchenpos.domain.vo.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @DisplayName("[SUCCESS] 생성한다.")
    @Test
    void success_create() {
        // given
        final OrderTable orderTable = new OrderTable(null, 10, true);

        // expect
        assertThatCode(() -> new Order(orderTable, OrderStatus.MEAL, LocalDateTime.now(), Collections.emptyList()))
                .doesNotThrowAnyException();
    }

    @DisplayName("[SUCCESS] 주문 항목을 추가할 경우 자신을 주문 항목에 주입한다.")
    @Test
    void success_addOrderLineItems() {
        // given
        final MenuGroup menuGroup = new MenuGroup(new Name("테스트용 메뉴 그룹명"));
        final Menu menu = new Menu(new Name("테스트용 메뉴명"), new Price("10000"), menuGroup, new ArrayList<>());

        final OrderTable orderTable = new OrderTable(null, 10, true);
        final Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), new ArrayList<>());

        // then
        order.addOrderLineItems(List.of(
                new OrderLineItem(null, menu, 10)
        ));

        // when
        final List<OrderLineItem> actual = order.getOrderLineItems();
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(1);
            final OrderLineItem actualOrderLineItem = actual.get(0);

            softly.assertThat(actualOrderLineItem)
                    .usingRecursiveComparison()
                    .isEqualTo(new OrderLineItem(order, menu, 10));
        });
    }
}
