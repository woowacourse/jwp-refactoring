package kitchenpos.domain;

import static kitchenpos.application.fixture.OrderTableFixture.NUMBER_OF_GUEST;
import static kitchenpos.application.fixture.OrderTableFixture.UNSAVED_ORDER_TABLE_EMPTY;
import static kitchenpos.application.fixture.OrderTableFixture.UNSAVED_ORDER_TABLE_NOT_EMPTY;
import static kitchenpos.application.fixture.OrderTableFixture.makeOrderTable;
import static kitchenpos.application.fixture.TableGroupFixture.TABLE_GROUP_ID_FOR_TEST;
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
        return Stream.of(
                Arguments.of(makeOrderTable(NUMBER_OF_GUEST, true, null), false),
                Arguments.of(makeOrderTable(NUMBER_OF_GUEST, false, null), true)
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
        return Stream.of(
                Arguments.of(makeOrderTable(NUMBER_OF_GUEST, true, TABLE_GROUP_ID_FOR_TEST), false),
                Arguments.of(makeOrderTable(NUMBER_OF_GUEST, false, TABLE_GROUP_ID_FOR_TEST), true)
        );
    }

    @DisplayName("테이블 인원 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        int numberOfGuestToChange = 7;
        OrderTable orderTable = makeOrderTable(NUMBER_OF_GUEST, false, TABLE_GROUP_ID_FOR_TEST);
        orderTable.changeNumberOfGuests(numberOfGuestToChange);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuestToChange);
    }

    @DisplayName("테이블 인원 수를 음수로 변경하려 하면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_Exception_Invalid_Value() {
        int invalidNumberOfGuest = -1;
        OrderTable orderTable = makeOrderTable(NUMBER_OF_GUEST, false, null);
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(invalidNumberOfGuest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 empty일때 인원수를 변경하려 하면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_Exception_Empty() {
        OrderTable orderTable = makeOrderTable(NUMBER_OF_GUEST, true, null);
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(3))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블을 group에서 해제한다.")
    @Test
    void exitFromGroup() {
        OrderTable orderTable = makeOrderTable(NUMBER_OF_GUEST, true, TABLE_GROUP_ID_FOR_TEST);

        orderTable.exitFromGroup();

        assertThat(orderTable.getTableGroupId()).isNull();
        assertThat(orderTable.isEmpty()).isFalse();
    }
}
