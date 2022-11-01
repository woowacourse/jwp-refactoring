package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블 도메인의")
class OrderTableTest {

    @Nested
    @DisplayName("group 메서드는")
    class Group {

        @Test
        @DisplayName("단체 지정 ID를 갖는 비어있지 않는 주문 테이블을 생성한다.")
        void group_tableGroupId_success() {
            // given
            final long tableGroupId = 7L;
            final OrderTable orderTable = OrderTable.of(1, true);

            // when
            final OrderTable expected = orderTable.group(tableGroupId);

            // then
            assertThat(expected).extracting(OrderTable::getTableGroupId, OrderTable::isEmpty)
                    .containsExactly(tableGroupId, false);
        }
    }

    @Nested
    @DisplayName("changeNumberOfGuests 메서드는")
    class ChangeNumberOfGuests {

        @Test
        @DisplayName("빈 주문 테이블의 방문한 손님 수를 변경할 수 없다.")
        void changeNumberOfGuests_emptyOrderTable_exception() {
            // given
            final OrderTable orderTable = OrderTable.of(1, true);

            // when & then
            assertThatThrownBy(() -> orderTable.changeNumberOfGuests(7))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("방문한 손님 수를 0명 미만으로 변경할 수 없다.")
        void changeNumberOfGuests_lessThanZero_exception() {
            // given
            final OrderTable orderTable = OrderTable.of(1,  false);

            // when & then
            assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("changeEmpty 메서드는")
    class ChangeEmpty {

        @Test
        @DisplayName("단체 지정된 테이블은 주문 등록 가능 여부를 변경할 수 없다.")
        void changeEmpty_tableGroupIdIsNotNull_exception() {
            // given
            final OrderTable orderTable = new OrderTable(1L, 7L, 1, false);

            // when, then
            assertThatThrownBy(() -> orderTable.changeEmpty(true))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("verifyCanGroup 메서드는")
    class VerifyCanGroup {

        @Test
        @DisplayName("비어있고 단체 지정 되어있지 않는 테이블을 통과시킨다.")
        void verifyCanGroup_emptyAndTableGroupIdIsNull_success() {
            // given
            final OrderTable orderTable = new OrderTable(1L, null, 1, true);

            // when, then
            assertThatCode(orderTable::verifyCanGroup)
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("비어있지 않은 주문 테이블에 대해서 예외를 던진다.")
        void verifyCanGroup_notEmptyOrderTable_exception() {
            // given
            final OrderTable orderTable = new OrderTable(1L, null, 1, false);

            // when, then
            assertThatThrownBy(orderTable::verifyCanGroup)
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("단체 지정 되어있는 주문 테이블에 대해서 예외를 던진다.")
        void verifyCanGroup_tableGroupIdIsNotNull_exception() {
            // given
            final OrderTable orderTable = new OrderTable(1L, 7L, 1, true);

            // when, then
            assertThatThrownBy(orderTable::verifyCanGroup)
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("ungroup 메서드는")
    class Ungroup {

        @Test
        @DisplayName("단체 지정 ID가 null이고 비어있지 않은 주문 테이블은 생성한다.")
        void ungroup_success() {
            // given
            final OrderTable orderTable = new OrderTable(1L, 7L, 1, true);

            // when
            final OrderTable expected = orderTable.ungroup();

            // then
            assertThat(expected).extracting(OrderTable::getTableGroupId, OrderTable::isEmpty)
                    .containsExactly(null, false);
        }
    }
}
