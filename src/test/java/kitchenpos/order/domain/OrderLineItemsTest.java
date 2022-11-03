package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Collections;
import java.util.List;
import kitchenpos.exception.InvalidOrderLineItemsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {
//
//    private OrderValidator orderValidator;
//
//    @BeforeEach
//    void setUp() {
//        orderValidator = Mockito.mock(OrderValidator.class);
//    }

    @Nested
    @DisplayName("주문 내역을 생성할 때 ")
    class CreateTest {

        @Test
        @DisplayName("비어있으면 예외가 발생한다.")
        void emptyFailed() {
            assertThatThrownBy(() -> new OrderLineItems(Collections.emptyList()))
                    .isInstanceOf(InvalidOrderLineItemsException.class)
                    .hasMessage("세부 주문 내역이 비어있습니다.");
        }

        @Test
        @DisplayName("정상적일 경우 성공한다.")
        void create() {
            assertDoesNotThrow(() -> new OrderLineItems(List.of(new OrderLineItem(1L, 1L, 1L, 10))));
        }
    }
}
