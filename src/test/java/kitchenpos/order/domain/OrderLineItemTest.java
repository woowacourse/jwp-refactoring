package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.order.exception.InvalidOrderLineItemException;
import kitchenpos.order.exception.InvalidOrderLineItemQuantityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("OrderLineItem 단위 테스트")
class OrderLineItemTest {

    private static final Long MENU_ID = 1L;
    private static final Long ORDER_TABLE_ID = 1L;

    @DisplayName("OrderLineItem을 생성할 때")
    @Nested
    class Create {

        @DisplayName("Order가 Null이면 예외가 발생한다.")
        @Test
        void oderNullException() {
            // when, then
            assertThatThrownBy(() -> new OrderLineItem(5L, null, MENU_ID))
                .isExactlyInstanceOf(InvalidOrderLineItemException.class);
        }

        @DisplayName("Menu가 Null이면 예외가 발생한다.")
        @Test
        void menuNullException() {
            // given
            Order order = new Order(ORDER_TABLE_ID);

            // when, then
            assertThatThrownBy(() -> new OrderLineItem(5L, order, null))
                .isExactlyInstanceOf(InvalidOrderLineItemException.class);
        }

        @DisplayName("Quantity가 Null이면 예외가 발생한다.")
        @Test
        void quantityNullException() {
            // given
            Order order = new Order(ORDER_TABLE_ID);

            // when, then
            assertThatThrownBy(() -> new OrderLineItem(null, order, MENU_ID))
                .isExactlyInstanceOf(InvalidOrderLineItemQuantityException.class);
        }

        @DisplayName("Quantity가 음수면 예외가 발생한다.")
        @Test
        void quantityNegativeException() {
            // given
            Order order = new Order(ORDER_TABLE_ID);

            // when, then
            assertThatThrownBy(() -> new OrderLineItem(-1L, order, MENU_ID))
                .isExactlyInstanceOf(InvalidOrderLineItemQuantityException.class);
        }
    }
}
