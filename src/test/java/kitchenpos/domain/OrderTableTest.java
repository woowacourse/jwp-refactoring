package kitchenpos.domain;

import static kitchenpos.fixture.DomainFixture.createTableGroup;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.GuestSizeException;
import kitchenpos.exception.TableGroupNotNullException;
import kitchenpos.exception.UnableToGroupingException;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void 테이블그룹이_존재하면_그룹화_예외를_발생한다() {
        OrderTable orderTable = new OrderTable(0, false);

        assertThatThrownBy(() -> orderTable.grouping(createTableGroup()))
                .isInstanceOf(UnableToGroupingException.class);
    }

    @Test
    void 비어있지_않으면_그룹화_예외를_발생한다() {
        OrderTable orderTable = new OrderTable(0, false);

        assertThatThrownBy(() -> orderTable.grouping(createTableGroup()))
                .isInstanceOf(UnableToGroupingException.class);
    }

    @Test
    void 테이블그룹이_존재하면_비울때_예외를_발생한다() {
        OrderTable orderTable = new OrderTable(createTableGroup(), 0, false);

        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(TableGroupNotNullException.class);
    }

    @Test
    void 비어있으면_손님수_변경시_예외를_발생한다() {
        OrderTable orderTable = new OrderTable(0, true);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(1))
                .isInstanceOf(GuestSizeException.class);
    }

    @Test
    void 손님수가_잘못되면_예외를_발생한다() {
        OrderTable orderTable = new OrderTable(0, false);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(GuestSizeException.class);
    }
}
