package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.vo.Name;
import kitchenpos.domain.vo.Price;
import kitchenpos.domain.vo.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

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
        assertThatCode(() -> Order.ofEmptyOrderLineItems(orderTable))
                .doesNotThrowAnyException();
    }

    @DisplayName(("[SUCCESS] 주문 상태가 COOKING 에 비어있는 주문 상품 항목 상태로 주문을 생성한다."))
    @Test
    void success_ofEmptyOrderLineItems() {
        // given
        final OrderTable orderTable = OrderTable.withoutTableGroup(10, false);
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
        final Menu menu = Menu.withEmptyMenuProducts(new Name("테스트용 메뉴명"), Price.ZERO, menuGroup);

        final OrderTable orderTable = OrderTable.withoutTableGroup(10, false);
        final Order order = Order.ofEmptyOrderLineItems(orderTable);

        // when
        order.addOrderLineItems(List.of(
                new OrderLineItem(menu, new Quantity(10))
        ));

        // then
        final List<OrderLineItem> actual = order.getOrderLineItems().getOrderLineItems();
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(1);
            final OrderLineItem actualOrderLineItem = actual.get(0);

            softly.assertThat(actualOrderLineItem)
                    .usingRecursiveComparison()
                    .isEqualTo(new OrderLineItem(menu, new Quantity(10)));
        });
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
            final Order order = Order.ofEmptyOrderLineItems(orderTable);

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
            final Order order = Order.ofEmptyOrderLineItems(orderTable);
            order.changeOrderStatus(OrderStatus.COMPLETION);

            // expect
            assertThatThrownBy(() -> order.changeOrderStatus(orderStatus))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 테이블 빈 상태 변경")
    @Nested
    class ChangeOrderStatusEmptyNestedClass {

        @DisplayName("[SUCCESS] 주문 테이블을 채울 수 있다.")
        @ParameterizedTest
        @EnumSource(OrderStatus.class)
        void success_changeOrderTableEmpty_full(final OrderStatus orderStatus) {
            // given
            final Order order = Order.ofEmptyOrderLineItems(OrderTable.withoutTableGroup(5, true));
            order.changeOrderStatus(orderStatus);

            // when
            order.changeOrderTableEmpty(false);

            // then
            assertThat(order.getOrderTable().isEmpty()).isFalse();
        }

        @DisplayName("[SUCCESS] 주문이 완료된 상태일 때 채워져 있는 주문 테이블을 비울 수 있다.")
        @Test
        void success_changeOrderTableEmpty_when_orderStatus_isCompletion() {
            // given
            final Order order = Order.ofEmptyOrderLineItems(OrderTable.withoutTableGroup(5, true));
            order.changeOrderStatus(OrderStatus.COMPLETION);

            // when
            order.changeOrderTableEmpty(true);

            // then
            assertThat(order.getOrderTable().isEmpty()).isTrue();
        }

        @DisplayName("[EXCEPTION] 주문이 완료된 상태가 아니라면 주문 테이블을 비울 수 없다.")
        @Test
        void throwException_changeOrderTableEmpty_when_orderStatus_isNotCompletion() {
            // given
            final Order order = Order.ofEmptyOrderLineItems(OrderTable.withoutTableGroup(5, true));

            // expected
            assertThatThrownBy(() -> order.changeOrderTableEmpty(true))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("주문 테이블 그룹화 해제")
    @Nested
    class UngroupOrderTableNestedTest {

        @DisplayName("[SUCCESS] 주문 테이블의 주문이 COMPLETION 일 경우 그룹화 해제할 수 있다.")
        @Test
        void success_ungroupOrderTable() {
            // given
            final OrderTable orderTableOne = OrderTable.withoutTableGroup(5, true);
            final OrderTable orderTableTwo = OrderTable.withoutTableGroup(5, true);
            TableGroup.withOrderTables(List.of(
                    orderTableOne,
                    orderTableTwo
            ));

            final Order order = Order.ofEmptyOrderLineItems(orderTableOne);
            order.changeOrderStatus(OrderStatus.COMPLETION);

            // when
            order.ungroupOrderTable();

            // then
            assertThat(order.getOrderTable().getTableGroup()).isNull();
        }

        @DisplayName("[EXCEPTION] 주문 테이블의 주문 상태가 COMPLETION 이 아닐 경우 그룹화 해제할 수 없다.")
        @ParameterizedTest
        @MethodSource("getOrderStatusWithoutCompletion")
        void throwException_ungroupOrderTable_when_orderStatus_isNotCompletion(final OrderStatus orderStatus) {
            // given
            final OrderTable orderTableOne = OrderTable.withoutTableGroup(5, true);
            final OrderTable orderTableTwo = OrderTable.withoutTableGroup(5, true);
            TableGroup.withOrderTables(List.of(
                    orderTableOne,
                    orderTableTwo
            ));

            final Order order = Order.ofEmptyOrderLineItems(orderTableOne);

            // when
            order.changeOrderStatus(orderStatus);

            // then
            assertThatThrownBy(order::ungroupOrderTable)
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private Stream<Arguments> getOrderStatusWithoutCompletion() {
            return Arrays.stream(OrderStatus.values())
                    .filter(orderStatus -> orderStatus != OrderStatus.COMPLETION)
                    .map(Arguments::arguments);
        }
    }
}
