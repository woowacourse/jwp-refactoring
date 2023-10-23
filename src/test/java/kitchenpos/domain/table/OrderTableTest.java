package kitchenpos.domain.table;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    void 단체지정을_해제한다() {
        // given
        OrderTable orderTable = new OrderTable(1L, 0, true);

        // when
        orderTable.ungroup();

        // then
        assertThat(orderTable.getTableGroupId()).isNull();
    }

    @Test
    void 단체지정을_할_때_이미_단체지정이_되어있으면_예외를_던진다() {
        // given
        OrderTable orderTable = new OrderTable(1L, 0, true);

        // expect
        assertThatThrownBy(orderTable::group)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비어있지 않거나, 이미 단체 지정이 된 테이블은 단체 지정을 할 수 없습니다.");
    }

    @Test
    void 단체지정을_할_때_테이블이_비어있지_않은_경우_예외를_던진다() {
        // given
        OrderTable orderTable = new OrderTable(null, 0, false);

        // expect
        assertThatThrownBy(orderTable::group)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비어있지 않거나, 이미 단체 지정이 된 테이블은 단체 지정을 할 수 없습니다.");
    }

    @Test
    void 단체_지정을_성공하는_경우_테이블의_상태가_비어있지_않은_테이블로_변경된다() {
        // given
        OrderTable orderTable = new OrderTable(null, 0, true);

        // when
        orderTable.group();

        // then
        assertThat(orderTable.isEmpty()).isFalse();
    }
}
