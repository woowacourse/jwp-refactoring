package kitchenpos.domain;

import static kitchenpos.application.fixture.OrderTableFixture.UNSAVED_ORDER_TABLE_EMPTY;
import static kitchenpos.application.fixture.OrderTableFixture.UNSAVED_ORDER_TABLE_NOT_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class OrderTableTest {

    @DisplayName("테이블의 empty 상태를 변경한다.")
    @ParameterizedTest
    @MethodSource("argsOfChangeEmpty")
    void changeEmpty(OrderTable orderTable, boolean reverseEmptyStatus) {
        orderTable.changeEmpty(reverseEmptyStatus);

        assertThat(orderTable.isEmpty()).isEqualTo(reverseEmptyStatus);
    }

    static Stream<Arguments> argsOfChangeEmpty() {
        UNSAVED_ORDER_TABLE_NOT_EMPTY.setTableGroupId(null);
        UNSAVED_ORDER_TABLE_EMPTY.setTableGroupId(null);
        return Stream.of(
                Arguments.of(UNSAVED_ORDER_TABLE_EMPTY, false),
                Arguments.of(UNSAVED_ORDER_TABLE_NOT_EMPTY, true)
        );
    }

    @DisplayName("테이블이 테이블그룹에 속해있을때 empty상태를 변경하면 예외가 발생한다.")
    @ParameterizedTest
    @MethodSource("argsOfChangeEmptyException")
    void changeEmpty_Exception(OrderTable orderTable, boolean reverseEmptyStatus) {
        assertThatThrownBy(() -> orderTable.changeEmpty(reverseEmptyStatus))
                .isInstanceOf(IllegalArgumentException.class);
    }

    static Stream<Arguments> argsOfChangeEmptyException() {
        UNSAVED_ORDER_TABLE_NOT_EMPTY.setTableGroupId(0L);
        UNSAVED_ORDER_TABLE_EMPTY.setTableGroupId(0L);
        return Stream.of(
                Arguments.of(UNSAVED_ORDER_TABLE_EMPTY, false),
                Arguments.of(UNSAVED_ORDER_TABLE_NOT_EMPTY, true)
        );
    }

    @DisplayName("테이블 인원 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        UNSAVED_ORDER_TABLE_NOT_EMPTY.setTableGroupId(null);
        UNSAVED_ORDER_TABLE_NOT_EMPTY.changeEmpty(false);
        int numberOfGuestToChange = 7;
        UNSAVED_ORDER_TABLE_NOT_EMPTY.changeNumberOfGuests(numberOfGuestToChange);

        assertThat(UNSAVED_ORDER_TABLE_NOT_EMPTY.getNumberOfGuests()).isEqualTo(numberOfGuestToChange);
    }

    @DisplayName("테이블 인원 수를 음수로 변경하려 하면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_Exception_Invalid_Value() {
        int invalidNumberOfGuest = -1;
        UNSAVED_ORDER_TABLE_NOT_EMPTY.setTableGroupId(null);
        UNSAVED_ORDER_TABLE_NOT_EMPTY.changeEmpty(false);
        assertThatThrownBy(() -> UNSAVED_ORDER_TABLE_NOT_EMPTY.changeNumberOfGuests(invalidNumberOfGuest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 empty일때 인원수를 변경하려 하면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_Exception_Empty() {
        UNSAVED_ORDER_TABLE_NOT_EMPTY.setTableGroupId(null);
        UNSAVED_ORDER_TABLE_EMPTY.changeEmpty(true);
        assertThatThrownBy(() -> UNSAVED_ORDER_TABLE_EMPTY.changeNumberOfGuests(3))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
