package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderTableTest {

    @Test
    void 방문_손님수를_변경하는_경우_음수를_입력받으면_예외를_던진다() {
        // given
        OrderTable orderTable = new OrderTable(0, true);

        // expect
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("방문한 손님 수는 음수가 될 수 없습니다.");
    }

    @Test
    void 방문_손님수를_변경하는_경우_빈_테이블인_경우_예외를_던진다() {
        // given
        OrderTable orderTable = new OrderTable(0, true);

        // expect
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블에는 손님을 지정할 수 없습니다.");
    }

    @Test
    void 테이블의_상태를_변경하는_경우_이미_단체지정이_되어있는_테이블이라면_예외를_던진다() {
        // given
        OrderTable orderTable = new OrderTable(1L, 0, true);

        // expect
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정이 되어있는 경우 테이블의 상태를 변경할 수 없습니다.");
    }
}
