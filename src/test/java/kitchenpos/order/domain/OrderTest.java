package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;
import kitchenpos.exception.CompletedOrderStatusChangeException;
import kitchenpos.exception.NotContainsOrderLineItemException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

class OrderTest {

    @Test
    @DisplayName("주문을 생성한다.")
    void of() {
        // given
        final OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1);
        final OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1);

        // when
        final Order order = Order.of(1L, Arrays.asList(orderLineItem1, orderLineItem2));

        // then
        assertAll(
                () -> assertThat(order.getOrderTableId()).isEqualTo(1L),
                () -> assertThat(order.getOrderedTime()).isNotNull(),
                () -> assertThat(order.getOrderStatus()).isEqualTo(COOKING)
        );
    }

    @Test
    @DisplayName("주문하려는 주문 항목이 0개면 예외를 발생한다.")
    void of_emptyOrderLineItem() {
        // given, when, then
        assertThatThrownBy(() -> Order.of(1L, Collections.emptyList()))
                .isExactlyInstanceOf(NotContainsOrderLineItemException.class);
    }

    @Test
    @DisplayName("주문 목록을 추가한다.")
    void addOrderLineItem() {
        // given
        final OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1);
        final OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1);
        final OrderLineItem orderLineItem3 = new OrderLineItem(3L, 1);
        final ArrayList<OrderLineItem> orderLineItems =
                new ArrayList<>(Arrays.asList(orderLineItem1, orderLineItem2));
        final Order order = Order.of(1L, orderLineItems);

        // when
        order.addOrderLineItem(orderLineItem3);

        // then
        assertThat(order.getOrderLineItems()).hasSize(3)
                .containsExactly(orderLineItem1, orderLineItem2, orderLineItem3);
    }

    @ParameterizedTest
    @MethodSource(value = "statusWithExpect")
    @DisplayName("주문이 완료 상태인지 확인한다.")
    void isNotComplete(final OrderStatus status, final boolean expect) {
        // given
        final OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1);
        final OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1);
        final Order order = Order.of(1L, Arrays.asList(orderLineItem1, orderLineItem2));
        order.setOrderStatus(status);

        // when
        final boolean actual = order.isNotComplete();

        // then
        assertThat(actual).isEqualTo(expect);
    }

    private static Stream<Arguments> statusWithExpect() {
        return Stream.of(
                arguments(COOKING, true),
                arguments(MEAL, true),
                arguments(COMPLETION, false)
        );
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class)
    @DisplayName("특정 주문의 상태가 변경할 수 있다.")
    void changeOrderStatus(final OrderStatus orderStatus) {
        // given
        final OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1);
        final OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1);
        final Order order = Order.of(1L, Arrays.asList(orderLineItem1, orderLineItem2));

        // when
        order.setOrderStatus(orderStatus);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(orderStatus);
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class)
    @DisplayName("특정 주문의 상태가 완료 상태면 변경할 수 없다.")
    void changeOrderStatus_exceptionChangeToCompletion(final OrderStatus orderStatus) {
        // given
        final OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1);
        final OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1);
        final Order order = Order.of(1L, Arrays.asList(orderLineItem1, orderLineItem2));
        order.setOrderStatus(COMPLETION);

        // when, then
        assertThatThrownBy(() -> order.setOrderStatus(orderStatus))
                .isExactlyInstanceOf(CompletedOrderStatusChangeException.class);
    }
}
