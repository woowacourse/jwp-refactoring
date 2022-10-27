package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class OrderTest {

    @Test
    @DisplayName("예외사항이 존재하지 않는 경우 객체를 생성한다.")
    void order() {
        assertDoesNotThrow(() -> new Order(1L));
    }

    @Test
    @DisplayName("주문 테이블 아이디가 비어있는 경우 예외가 발생한다.")
    void nullOrderTableId() {
        assertThatThrownBy(() -> new Order(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("주문 테이블은 비어있을 수 없습니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("orderLineItems가 비어있는 경우 예외가 발생한다.")
    void emptyOrderItemLines(List<OrderLineItem> items) {
        Order order = new Order(1L);
        assertThatThrownBy(() -> order.setOrderLineItems(items))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("주문 항목은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("COMPLETION 상태인 order를 변경하려는 경우 예외가 발생한다.")
    void completionStatus() {
        // given
        Order order = new Order(1L);
        order.changeStatus("COMPLETION");

        // when, then
        assertThatThrownBy(() -> order.changeStatus("COOKING"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("이미 완료된 주문 상태를 변경할 수 없습니다.");
    }

}
