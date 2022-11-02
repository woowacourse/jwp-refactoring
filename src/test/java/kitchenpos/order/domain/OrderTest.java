package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import kitchenpos.order.dto.application.OrderLineItemDto;

class OrderTest {

    private final List<OrderLineItemDto> orderLineItems = new ArrayList<OrderLineItemDto>() {{
        add(new OrderLineItemDto("menu", BigDecimal.valueOf(1000L), 1L));
    }};

    @Nested
    @DisplayName("객체 생성시")
    class CreateClass {

        @Test
        @DisplayName("예외사항이 존재하지 않는 경우 객체를 생성한다.")
        void order() {
            assertDoesNotThrow(() -> new Order(1L, orderLineItems));
        }

        @Test
        @DisplayName("주문 테이블이 비어있는 경우 예외가 발생한다.")
        void nullOrderTableId() {
            assertThatThrownBy(() -> new Order(null, new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블은 비어있을 수 없습니다.");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("orderLineItems가 비어있는 경우 예외가 발생한다.")
        void emptyOrderItemLines(List<OrderLineItemDto> orderLineItems) {
            assertThatThrownBy(() -> new Order(1L, orderLineItems))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 항목은 비어있을 수 없습니다.");
        }

    }

    @Nested
    @DisplayName("changeStatus")
    class ChangeStatusMethod {

        @Test
        @DisplayName("예외사항이 존재하지 않는 경우 status를 변경한다.")
        void changeStatus() {
            // given
            Order order = new Order(1L, orderLineItems);

            // when
            order.changeStatus(OrderStatus.COMPLETION.name());

            // then
            assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
        }

        @Test
        @DisplayName("COMPLETION 상태인 order를 변경하려는 경우 예외가 발생한다.")
        void completionStatus() {
            // given
            Order order = new Order(1L, orderLineItems);
            order.changeStatus(OrderStatus.COMPLETION.name());

            // when, then
            assertThatThrownBy(() -> order.changeStatus("COOKING"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 완료된 주문 상태를 변경할 수 없습니다.");
        }

    }

}
