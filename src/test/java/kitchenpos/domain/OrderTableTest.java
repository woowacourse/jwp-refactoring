package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Nested
    @DisplayName("OrderTable이 사용 중인지 확인할 때 ")
    class IsUsingTest {

        @Test
        @DisplayName("비어있지 않을 경우 사용 중이다.")
        void emptyUsing() {
            OrderTable orderTable = new OrderTable(10, false);

            assertThat(orderTable.isUsing()).isTrue();
        }

        @Test
        @DisplayName("tableGroupId가 있을 경우 사용 중이다.")
        void hasTableUsing() {
            OrderTable orderTable = new OrderTable(1L, 10, false);

            assertThat(orderTable.isUsing()).isTrue();
        }

        @Test
        @DisplayName("사용 중이지 않다.")
        void isNotUsing() {
            OrderTable orderTable = new OrderTable(10, true);

            assertThat(orderTable.isUsing()).isFalse();
        }
    }
}
