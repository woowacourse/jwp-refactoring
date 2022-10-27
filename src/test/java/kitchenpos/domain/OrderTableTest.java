package kitchenpos.domain;

import static kitchenpos.support.Fixture.createEmptyOrderTable;
import static kitchenpos.support.Fixture.createGroupedOrderTable;
import static kitchenpos.support.Fixture.createOrderTableWithNumberOfGuests;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import kitchenpos.support.Fixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("OrderTable 의")
class OrderTableTest {

    @Nested
    @DisplayName("updateEmpty 메서드는")
    class UpdateEmpty {
        @Test
        @DisplayName("테이블 비움상태를 수정한다.")
        void success() {
            //given
            OrderTable orderTable = createEmptyOrderTable();

            //when
            orderTable.updateEmpty(false);

            //then
            assertThat(orderTable.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("테이블이 그룹에 속해있으면, 예외를 던진다.")
        void fail_existGroupId() {
            //given
            OrderTable orderTable = createGroupedOrderTable();

            //when & then
            assertThatThrownBy(() -> orderTable.updateEmpty(false))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("updateNumberOfGuests 메서드는")
    class UpdateNumberOfGuests {

        @Test
        @DisplayName("테이블 인원을 수정한다.")
        void success() {
            //given
            OrderTable orderTable = createOrderTableWithNumberOfGuests(2);
            int expected = 4;

            //when
            orderTable.updateNumberOfGuests(expected);

            //then
            assertThat(orderTable.getNumberOfGuests()).isEqualTo(expected);
        }

        @Test
        @DisplayName("테이블이 비어있으면, 예외를 던진다.")
        void fail_empty() {
            //given
            OrderTable orderTable = Fixture.createEmptyOrderTable();

            //when & then
            assertThatThrownBy(() -> orderTable.updateNumberOfGuests(2))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("checkCanGroup 메서드는")
    class CheckCanGroup {

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
            assertThatThrownBy(orderTable::checkCanGroup)
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("비어있으면, 예외를 던진다.")
        void fail_isEmpty() {
            //given
            OrderTable orderTable = new OrderTable(1L, null, 2, false);

            //when & then
            assertThatThrownBy(orderTable::checkCanGroup)
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
