package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderTableTest {

    @Nested
    class 테이블_인원_변경시 {

        @ParameterizedTest
        @ValueSource(ints = {3, 6, 10})
        void 성공(int numberOfGuests) {
            // given
            OrderTable orderTable = new OrderTable(5, false);

            // when
            orderTable.changeNumberOfGuests(numberOfGuests);

            // then
            assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
        }

        @ParameterizedTest
        @ValueSource(ints = {-100, -1})
        void 음수로_요청하면_예외(int numberOfGuests) {
            // given
            OrderTable orderTable = new OrderTable(5, false);

            // when && then
            assertThatThrownBy(() -> orderTable.changeNumberOfGuests(numberOfGuests))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("테이블 인원은 양수여야합니다.");
        }

        @Test
        void 테이블이_비어있으면_예외() {
            // given
            OrderTable orderTable = new OrderTable(5, true);

            // when && then
            assertThatThrownBy(() -> orderTable.changeNumberOfGuests(3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비어있는 테이블의 인원을 변경할 수 없습니다.");
        }
    }

    @Nested
    class 테이블_상태_변경시 {

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        void 성공(boolean empty) {
            // given
            OrderTable orderTable = new OrderTable(1L, null, 5, !empty);

            // when
            orderTable.changeEmpty(empty);

            // then
            assertThat(orderTable.isEmpty()).isEqualTo(empty);
        }

        @Test
        void 연관된_테이블_그룹이_존재하면_예외() {
            // given
            OrderTable orderTable = new OrderTable(1L, 1L, 5, true);

            // when && then
            assertThatThrownBy(() -> orderTable.changeEmpty(false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("테이블그룹에 속한 테이블의 상태를 변경할 수 없습니다.");
        }
    }

    @Nested
    class 테이블을_그룹화_할때 {

        @Test
        void 성공() {
            // given
            TableGroup tableGroup = new TableGroup(1L);
            OrderTable orderTable = new OrderTable(5, true);

            // when
            orderTable.group(tableGroup);

            // then
            assertAll(
                () -> assertThat(orderTable.getTableGroupId()).isEqualTo(1L),
                () -> assertThat(orderTable.isEmpty()).isFalse()
            );
        }

        @Test
        void 테이블이_비어있지_않으면_예외() {
            // given
            TableGroup tableGroup = new TableGroup(1L);
            OrderTable orderTable = new OrderTable(5, false);

            // when && then
            assertThatThrownBy(() -> orderTable.group(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비어있는 테이블만 주문그룹이 될 수 있습니다.");
        }

        @Test
        void 이미_그룹화된_테이블이면_예외() {
            // given
            TableGroup tableGroup = new TableGroup(1L);
            OrderTable orderTable = new OrderTable(1L, 2L,5, true);

            // when && then
            assertThatThrownBy(() -> orderTable.group(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("그룹에 속하지 않은 테이블만 주문그룹이 될 수 있습니다.");
        }
    }

    @Test
    void 테이블_그룹을_해제한다() {
        // given
        OrderTable orderTable = new OrderTable(1L, 1L, 3, false);

        // when
        orderTable.unGroup();

        // then
        assertAll(
            () -> assertThat(orderTable.getTableGroupId()).isNull(),
            () -> assertThat(orderTable.isEmpty()).isTrue()
        );
    }
}
