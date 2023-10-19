package kitchenpos.domain;

import kitchenpos.domain.vo.Name;
import kitchenpos.domain.vo.Price;
import kitchenpos.domain.vo.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class OrderTest {

    @DisplayName("[SUCCESS] 생성한다.")
    @Test
    void success_create() {
        // given
        final OrderTable orderTable = new OrderTable(null, 10, true);

        // expect
        assertThatCode(() -> new Order(orderTable, OrderStatus.MEAL, LocalDateTime.now(), new OrderLineItems(Collections.emptyList())))
                .doesNotThrowAnyException();
    }

    @DisplayName(("[SUCCESS] 주문 상태가 COOKING 에 비어있는 주문 상품 항목 상태로 주문을 생성한다."))
    @Test
    void ofEmptyOrderLineItems() {
        // given
        final OrderTable orderTable = new OrderTable(null, 10, true);
        final Order actual = Order.ofEmptyOrderLineItems(orderTable);

        // expect
        assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isNull();
            softly.assertThat(actual.getOrderTable()).isEqualTo(orderTable);
            softly.assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
            softly.assertThat(actual.getOrderedTime()).isBefore(LocalDateTime.now());
            softly.assertThat(actual.getOrderLineItems().getOrderLineItems()).isEmpty();
        });
    }

    @DisplayName("[SUCCESS] 주문 항목을 추가할 경우 자신을 주문 항목에 추가한다.")
    @Test
    void success_addOrderLineItems() {
        // given
        final MenuGroup menuGroup = new MenuGroup(new Name("테스트용 메뉴 그룹명"));
        final Menu menu = new Menu(new Name("테스트용 메뉴명"), Price.ZERO, menuGroup, MenuProducts.empty());

        final OrderTable orderTable = new OrderTable(null, 10, true);
        final Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), new OrderLineItems(new ArrayList<>()));

        // when
        order.addOrderLineItems(List.of(
                new OrderLineItem(null, menu, new Quantity(10))
        ));

        // then
        final List<OrderLineItem> actual = order.getOrderLineItems().getOrderLineItems();
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(1);
            final OrderLineItem actualOrderLineItem = actual.get(0);

            softly.assertThat(actualOrderLineItem)
                    .usingRecursiveComparison()
                    .isEqualTo(new OrderLineItem(order, menu, new Quantity(10)));
        });
    }

    @DisplayName("[EXCEPTION] 주문 완료 상태에서 주문 완료 상태로 변경할 경우 예외가 발생한다.")
    @Test
    void throwException_when_changeOrderStatus_Completion_to_Completion() {
        // given
        final OrderTable orderTable = new OrderTable(null, 10, true);
        final Order order = Order.ofEmptyOrderLineItems(orderTable);
        order.changeOrderStatus(OrderStatus.COMPLETION);

        // expect
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COMPLETION))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
