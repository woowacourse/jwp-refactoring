package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("OrderTable 의")
class OrderTableTest {

    @Nested
    @DisplayName("checkCanGroup 메서드는")
    class checkCanGroup {

        @Test
        @DisplayName("그룹이 지정되어있지 않고 비어있는지 확인한다.")
        void success() {
            //given
            OrderTable orderTable = new OrderTable(1L, null, 0, true);

            //when & then
            assertDoesNotThrow(orderTable::checkCanGroup);
        }

        @Test
        @DisplayName("그룹이 있으면, 예외를 던진다.")
        void fail_existGroup() {
            //given
            OrderTable orderTable = new OrderTable(1L, 1L, 0, true);

            //when & then
            Assertions.assertThatThrownBy(orderTable::checkCanGroup)
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("비어있으면, 예외를 던진다.")
        void fail_isEmpty() {
            //given
            OrderTable orderTable = new OrderTable(1L, null, 2, false);

            //when & then
            Assertions.assertThatThrownBy(orderTable::checkCanGroup)
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
