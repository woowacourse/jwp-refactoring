package kitchenpos.table.domain;

import static kitchenpos.support.fixture.TableFixture.비어있는_주문_테이블;
import static kitchenpos.support.fixture.TableFixture.비어있지_않는_주문_테이블;
import static kitchenpos.table.domain.exception.OrderTableExceptionType.NUMBER_OF_GUEST_LOWER_THAN_ZERO;
import static kitchenpos.table.domain.exception.OrderTableExceptionType.TABLE_CANT_CHANGE_EMPTY_ALREADY_IN_GROUP;
import static kitchenpos.table.domain.exception.OrderTableExceptionType.TABLE_CANT_CHANGE_NUMBER_OF_GUESTS_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.table.domain.exception.OrderTableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class OrderTableTest {

    @Test
    @DisplayName("OrderTable의 numberOfGuest가 0이하면 예외처리한다.")
    void validateNumberOfGuest() {
        assertThatThrownBy(() -> new OrderTable(null, -1, true))
            .isInstanceOf(OrderTableException.class)
            .hasMessage(NUMBER_OF_GUEST_LOWER_THAN_ZERO.getMessage());
    }

    @Nested
    @DisplayName("orderTable의 empty 상태를 변경할 수 있다.")
    class ChangeEmpty {

        @Test
        @DisplayName("정상적으로 변경하는 경우")
        void success() {
            final OrderTable orderTable = new OrderTable(null, 0, true);

            orderTable.changeEmpty(false);

            assertThat(orderTable.isEmpty())
                .isFalse();
        }

        @Test
        @DisplayName("tableGroup에 속해있는 orderTalbe인 경우 예외처리 한다.")
        void throwExceptionTableGroupIdIsNull() {
            final TableGroup mockTableGroup = Mockito.mock(TableGroup.class);
            final OrderTable orderTable = new OrderTable(mockTableGroup, 0, true);

            assertThatThrownBy(() -> orderTable.changeEmpty(false))
                .isInstanceOf(OrderTableException.class)
                .hasMessage(TABLE_CANT_CHANGE_EMPTY_ALREADY_IN_GROUP.getMessage());
        }
    }

    @Nested
    @DisplayName("numberOfGuest를 변경한다.")
    class ChangeNumberOfGuests {

        @Test
        @DisplayName("정상적으로 변경하는 경우")
        void success() {
            final OrderTable table = 비어있지_않는_주문_테이블();
            final int numberOfGuests = 10;

            table.changeNumberOfGuests(numberOfGuests);

            assertThat(table.getNumberOfGuests())
                .isEqualTo(10);
        }

        @Test
        @DisplayName("table이 empty인 경우 예외처리한다.")
        void throwExceptionTableIsEmpty() {
            final OrderTable emptyTable = 비어있는_주문_테이블();

            assertThatThrownBy(() -> emptyTable.changeNumberOfGuests(10))
                .isInstanceOf(OrderTableException.class)
                .hasMessage(TABLE_CANT_CHANGE_NUMBER_OF_GUESTS_EMPTY.getMessage());
        }

        @Test
        @DisplayName("table의 손님 수가 0보다 작은 경우 예외처리.")
        void throwExceptionNumberOfGuestIsLowerThanZero() {
            final OrderTable emptyTable = 비어있는_주문_테이블();

            assertThatThrownBy(() -> emptyTable.changeNumberOfGuests(-1))
                .isInstanceOf(OrderTableException.class)
                .hasMessage(NUMBER_OF_GUEST_LOWER_THAN_ZERO.getMessage());
        }
    }
}
