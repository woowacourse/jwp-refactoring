package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.common.vo.Name;
import kitchenpos.common.vo.Price;
import kitchenpos.common.vo.Quantity;
import kitchenpos.order.stub.OrderTableEmptyValidatorStub;
import kitchenpos.order.stub.OrderValidatorStub;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class OrderTest {

    @DisplayName("[SUCCESS] 생성한다.")
    @Test
    void success_create() {
        // given
        final OrderTable orderTable = OrderTable.withoutTableGroup(10, false);

        // expect
        assertThatCode(() -> Order.ofEmptyOrderLineItems(orderTable.getId()))
                .doesNotThrowAnyException();
    }

    @DisplayName(("[SUCCESS] 주문 상태가 COOKING 에 비어있는 주문 상품 항목 상태로 주문을 생성한다."))
    @Test
    void success_ofEmptyOrderLineItems() {
        // given
        final OrderTable orderTable = OrderTable.withoutTableGroup(10, false);
        final Order actual = Order.ofEmptyOrderLineItems(orderTable.getId());

        // expect
        assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isNull();
            softly.assertThat(actual.getOrderTableId()).isEqualTo(orderTable.getId());
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
        final Menu menu = Menu.withEmptyMenuProducts(new Name("테스트용 메뉴명"), Price.ZERO, menuGroup);

        final OrderTable orderTable = OrderTable.withoutTableGroup(10, false);
        final Order order = Order.ofEmptyOrderLineItems(orderTable.getId());

        // when
        order.addOrderLineItems(List.of(
                new OrderLineItem(menu.getId(), new Quantity(10))
        ));

        // then
        final List<OrderLineItem> actual = order.getOrderLineItems().getOrderLineItems();
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(1);
            final OrderLineItem actualOrderLineItem = actual.get(0);

            softly.assertThat(actualOrderLineItem)
                    .usingRecursiveComparison()
                    .isEqualTo(new OrderLineItem(menu.getId(), new Quantity(10)));
        });
    }

    @DisplayName("[SUCCESS] 주문을 준비한다.")
    @Test
    void success_prepare() {
        // given
        final MenuGroup menuGroup = new MenuGroup(new Name("테스트용 메뉴 그룹명"));
        final Menu menu = Menu.withEmptyMenuProducts(new Name("테스트용 메뉴명"), Price.ZERO, menuGroup);

        final OrderTable orderTable = OrderTable.withoutTableGroup(10, false);
        final Order order = Order.ofEmptyOrderLineItems(orderTable.getId());

        // when
        order.prepare(new OrderValidatorStub(), new OrderTableEmptyValidatorStub());

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @DisplayName("주문 상태 변경")
    @Nested
    class ChangeOrderStatusNestedClass {

        @DisplayName("[SUCCESS] 주문 상태를 변경한다.")
        @ParameterizedTest
        @EnumSource(OrderStatus.class)
        void success_changeOrderStatus(final OrderStatus orderStatus) {
            // given
            final OrderTable orderTable = OrderTable.withoutTableGroup(5, false);
            final Order order = Order.ofEmptyOrderLineItems(orderTable.getId());

            // when
            order.changeOrderStatus(orderStatus);

            // then
            assertThat(order.getOrderStatus()).isEqualTo(orderStatus);
        }

        @DisplayName("[EXCEPTION] 주문 완료 상태에서 주문 상태를 변경할 경우 예외가 발생한다.")
        @ParameterizedTest
        @EnumSource(OrderStatus.class)
        void throwException_when_changeOrderStatus_Completion_to_Completion(final OrderStatus orderStatus) {
            // given
            final OrderTable orderTable = OrderTable.withoutTableGroup(10, false);
            final Order order = Order.ofEmptyOrderLineItems(orderTable.getId());
            order.changeOrderStatus(OrderStatus.COMPLETION);

            // expect
            assertThatThrownBy(() -> order.changeOrderStatus(orderStatus))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
