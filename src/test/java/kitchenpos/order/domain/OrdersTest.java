package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import kitchenpos.order.exception.InvalidOrderStatusException;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Orders 단위 테스트")
class OrdersTest {

    @DisplayName("식사가 완료되지 않은 주문이 있는지 검증할 때")
    @Nested
    class ValidateCompletion {

        @DisplayName("아직 요리 중이거나 식사중인 주문이 있다면 예외가 발생한다.")
        @Test
        void validateCompletionException() {
            // given
            OrderTable orderTable = new OrderTable(5, true);

            Order order1 = new Order(orderTable);
            Order order2 = new Order(orderTable);
            order2.changeStatus(COMPLETION);

            Orders orders = new Orders(Arrays.asList(order1, order2));

            // when, then
            assertThatThrownBy(orders::validateCompleted)
                .isExactlyInstanceOf(InvalidOrderStatusException.class);
        }

        @DisplayName("모든 주문이 완료 상태라면 예외가 발생하지 않는다.")
        @Test
        void success() {
            // given
            OrderTable orderTable = new OrderTable(5, true);

            Order order1 = new Order(orderTable);
            Order order2 = new Order(orderTable);
            order1.changeStatus(COMPLETION);
            order2.changeStatus(COMPLETION);

            Orders orders = new Orders(Arrays.asList(order1, order2));

            // when, then
            assertThatCode(orders::validateCompleted)
                .doesNotThrowAnyException();
        }
    }
}
