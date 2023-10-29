package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTableTest {

    @Test
    void 테이블의_인원이_음수일_경우_예외가_발생한다() {
        //given
        int numberOfGuests = -1;
        boolean empty = true;

        //expect
        assertThatThrownBy(() -> new OrderTable(numberOfGuests, empty))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_인원을_음수로_설정하면_예외가_발생한다() {
        //given
        int numberOfGuests = 1;
        boolean empty = true;
        OrderTable orderTable = new OrderTable(numberOfGuests, empty);

        //expect
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블이_empty일때_인원수를_변경할_수_없다() {
        //given
        int numberOfGuests = 1;
        boolean empty = true;
        OrderTable orderTable = new OrderTable(numberOfGuests, empty);

        //expect
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 그룹을_없앨_수_있다() {
        //given
        OrderTable 테이블 = new OrderTable(1, true);
        테이블.changeTableGroup(1L);

        //when
        테이블.ungroup();

        //then
        assertAll(
                () -> assertThat(테이블.isEmpty()).isFalse(),
                () -> assertThat(테이블.getTableGroupId()).isNull()
        );
    }

    @Test
    void id가_같으면_동등하다() {
        //given
        OrderTable 테이블 = new OrderTable(1L, 1L, 1, true);

        //when
        boolean actual = 테이블.equals(new OrderTable(1L, 1L, 1, true));

        //then
        assertThat(actual).isTrue();
    }
}
