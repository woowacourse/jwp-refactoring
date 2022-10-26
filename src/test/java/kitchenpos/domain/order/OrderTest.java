package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import kitchenpos.domain.table.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderTest {

    @DisplayName("of 메서드는 특정 주문 테이블에 대해 신규 주문을 생성한다.")
    @Nested
    class OfTest {

        @Test
        void 생성된_신규_주문은_COOKING_상태를_지닌다() {
            OrderTable orderTable = new OrderTable(5, false);

            Order actual = Order.of(orderTable);
            Order expected = new Order(null, orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now());

            assertThat(actual).usingRecursiveComparison().ignoringFieldsOfTypes(LocalDateTime.class)
                    .isEqualTo(expected);
        }

        @Test
        void 비어있는_테이블에_대해_주문을_생성하려는_경우_예외발생() {
            OrderTable emptyTable = new OrderTable(0, true);

            assertThatThrownBy(() -> Order.of(emptyTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
