package kitchenpos.domain;

import static kitchenpos.exception.OrderTableExceptionType.ILLEGAL_CHANGE_NUMBER_OF_GUESTS;
import static kitchenpos.exception.OrderTableExceptionType.NUMBER_OF_GUESTS_IS_NULL_OR_NEGATIVE_EXCEPTION;
import static kitchenpos.exception.OrderTableExceptionType.TABLE_GROUP_IS_NOT_NULL_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import kitchenpos.exception.BaseExceptionType;
import kitchenpos.exception.OrderTableException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class OrderTableTest {

    @Test
    void empty_상태를_변경할_때_tableGroup을_가지면_예외_발생() {
        // given
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        OrderTable orderTable = new OrderTable(1L, tableGroup, 10, false);

        // when
        BaseExceptionType exceptionType = assertThrows(OrderTableException.class, () ->
                orderTable.changeEmpty(true)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(TABLE_GROUP_IS_NOT_NULL_EXCEPTION);
    }

    @NullSource
    @ParameterizedTest
    @ValueSource(ints = {-1})
    void 손님_수_변경할_때_변경하려는_수가_0이거나_널이면_예외_발생(Integer numberOfGuests) {
        // given
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        OrderTable orderTable = new OrderTable(1L, tableGroup, 10, false);

        // when
        BaseExceptionType exceptionType = assertThrows(OrderTableException.class, () ->
                orderTable.changeNumberOfGuests(numberOfGuests)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(NUMBER_OF_GUESTS_IS_NULL_OR_NEGATIVE_EXCEPTION);
    }

    @Test
    void 손님_수_변경할_때_테이블_상태가_Empty이면_예외_발생() {
        // given
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        OrderTable orderTable = new OrderTable(1L, tableGroup, 10, true);

        // when
        BaseExceptionType exceptionType = assertThrows(OrderTableException.class, () ->
                orderTable.changeNumberOfGuests(100)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(ILLEGAL_CHANGE_NUMBER_OF_GUESTS);
    }
}
