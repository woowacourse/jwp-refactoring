package kitchenpos.domain;

import kitchenpos.domain.exception.InvalidUpdateNumberOfGuestsException;
import org.assertj.core.api.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("주문 테이블 테스트")
class OrderTableTest {

    @Nested
    class 주문_테이블_생성 {

        @Test
        void 주문_테이블을_생성한다() {
            // when
            final OrderTable orderTable = new OrderTable(0, true);

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(orderTable.getNumberOfGuests()).isEqualTo(0);
                softAssertions.assertThat(orderTable.isEmpty()).isTrue();
            });
        }
    }

    @Nested
    class 주문_테이블의_그룹_해제 {

        @Test
        void 주문_테이블의_그룹을_해제한다() {
            // given
            final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of());
            final OrderTable orderTable = new OrderTable(0, true);
            orderTable.updateTableGroup(tableGroup);

            // when
            orderTable.ungroup();

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(orderTable.getTableGroup()).isNull();
                softAssertions.assertThat(orderTable.isEmpty()).isFalse();
            });
        }
    }

    @Nested
    class 주문_테이블의_사용자수를_수정 {

        @ParameterizedTest
        @ValueSource(ints = {0, 1})
        void 주문_테이블의_사용자수를_수정한다(final int numberOfGuests) {
            // given
            final OrderTable orderTable = new OrderTable(0, true);

            // when
            orderTable.updateNumberOfGuests(numberOfGuests);


            // then
            assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
        }

        @Test
        void 주문_테이블의_사용자수를_음수로_수정하면_예외를_반환한다() {
            // given
            final OrderTable orderTable = new OrderTable(0, true);

            // when & then
            assertThatThrownBy(() -> orderTable.updateNumberOfGuests(-5))
                    .isInstanceOf(InvalidUpdateNumberOfGuestsException.class)
                    .hasMessage("방문한 손님 수는 음수가 될 수 없습니다.");
        }
    }

    @Nested
    class 주문_테이블의_empty_상태_변경 {

        @Test
        void 주문_테이블의_empty_상태를_참에서_거짓으로_변경할_수_있다() {
            // given
            final OrderTable orderTable = new OrderTable(0, true);

            // when
            orderTable.updateEmpty(false);

            // then
            assertThat(orderTable.isEmpty()).isFalse();
        }

        @Test
        void 주문_테이블의_empty_상태를_거짓에서_참으로_변경할_수_있다() {
            // given
            final OrderTable orderTable = new OrderTable(0, false);

            // when
            orderTable.updateEmpty(true);

            // then
            assertThat(orderTable.isEmpty()).isTrue();
        }
    }
}
