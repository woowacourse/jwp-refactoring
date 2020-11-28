package kitchenpos.order.model;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import kitchenpos.ordertable.model.OrderTable;
import kitchenpos.orderline.model.OrderLineItem;

class OrderVerifierTest {
    @DisplayName("결제 완료되지 않은 테이블이 존재하면 예외 발생")
    @ParameterizedTest
    @CsvSource({"COOKING", "MEAL"})
    void validateOrderStatus_CompletionStatus_ThrownException(OrderStatus orderStatus) {
        Order order = new Order(1L, 1L, orderStatus, LocalDateTime.now());

        assertThatThrownBy(() -> OrderVerifier.validateNotCompleteOrderStatus(Collections.singletonList(order)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("테이블의 주문이 아직 결제되지 않았습니다.");
    }

    @DisplayName("비어있는 테이블에 주문이 들어오면 예외 발생")
    @Test
    void validateOrderCreation_EmptyTable_ThrownException() {
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1);
        OrderTable orderTable = new OrderTable(1L, null, 0, true);

        assertThatThrownBy(
            () -> OrderVerifier.validateOrderCreation(Collections.singletonList(orderLineItem), 1, orderTable))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("비어있는 테이블에는 주문할 수 없습니다.");
    }

    @DisplayName("존재하지 않는 메뉴로 주문이 들어오면 예외 발생")
    @Test
    void validateOrderCreation_MenuExistence_ThrownException() {
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1);
        OrderTable orderTable = new OrderTable(1L, null, 1, false);

        assertThatThrownBy(
            () -> OrderVerifier.validateOrderCreation(Collections.singletonList(orderLineItem), 0, orderTable))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 메뉴로 주문할 수 없습니다.");
    }

    @DisplayName("0개 메뉴의 주문이 들어오면 예외 발생")
    @Test
    void validateOrderCreation_MinimumMenuCount_ThrownException() {
        OrderTable orderTable = new OrderTable(1L, null, 1, false);

        assertThatThrownBy(
            () -> OrderVerifier.validateOrderCreation(Collections.EMPTY_LIST, 0, orderTable))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("주문은 1개 이상의 메뉴를 포함해야 합니다.");
    }
}