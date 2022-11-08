package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.exception.CompletedOrderStatusChangeException;
import kitchenpos.exception.NotContainsOrderLineItemException;
import kitchenpos.order.OrderPrice;
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
        final List<OrderLineItem> orderLineItems = getOrderLineItems();

        // when
        final Order order = Order.of(1L, orderLineItems);

        // then
        assertAll(
                () -> assertThat(order.getOrderTableId()).isEqualTo(1L),
                () -> assertThat(order.getOrderedTime()).isNotNull(),
                () -> assertThat(order.getOrderStatus()).isEqualTo(COOKING)
        );
    }

    private static List<OrderLineItem> getOrderLineItems() {
        final OrderLineItem orderLineItem1 = new OrderLineItem(
                "orderLine1", new OrderPrice(1000L), 1);
        final OrderLineItem orderLineItem2 = new OrderLineItem(
                "orderLine2", new OrderPrice(2000L), 1);
        return Arrays.asList(orderLineItem1, orderLineItem2);
    }

    @Test
    @DisplayName("주문하려는 주문 항목이 0개면 예외를 발생한다.")
    void of_emptyOrderLineItem() {
        // given, when, then
        assertThatThrownBy(() -> Order.of(1L, Collections.emptyList()))
                .isExactlyInstanceOf(NotContainsOrderLineItemException.class);
    }

    @ParameterizedTest
    @MethodSource(value = "statusWithExpect")
    @DisplayName("주문이 완료 상태인지 확인한다.")
    void isNotComplete(final OrderStatus status, final boolean expect) {
        // given
        final Order order = Order.of(1L, getOrderLineItems());
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
        final Order order = Order.of(1L, getOrderLineItems());

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
        final Order order = Order.of(1L, getOrderLineItems());
        order.setOrderStatus(COMPLETION);

        // when, then
        assertThatThrownBy(() -> order.setOrderStatus(orderStatus))
                .isExactlyInstanceOf(CompletedOrderStatusChangeException.class);
    }
}
