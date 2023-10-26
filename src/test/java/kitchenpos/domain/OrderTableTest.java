package kitchenpos.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable();
    }

    @Test
    @DisplayName("테이블의 빈 상태를 변경할 수 있다.")
    void updateEmpty() {
        //given
        final boolean isEmpty = true;

        //when
        orderTable.updateEmpty(isEmpty);

        //then
        assertThat(orderTable.isEmpty()).isEqualTo(isEmpty);
    }

    @Test
    @DisplayName("테이블 그룹이 있는 경우, 테이블 그룹이 null이 아닌 예외를 던진다.")
    void validateTableGroupIsNonNull_WithTableGroup() {
        //given
        //when
        //then
        assertThatThrownBy(() -> orderTable.validateTableGroupIsNonNull())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹이 null인 경우, 테이블 그룹이 null이 아닌 예외를 던진다.")
    void validateTableGroupIsNonNull_WithoutTableGroup() {
        //given
        //when
        //then
        assertThatThrownBy(() -> orderTable.validateTableGroupIsNonNull())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("손님 수를 업데이트할 수 있다.")
    void updateNumberOfGuests() {
        //given
        final int numberOfGuests = 4;

        //when
        orderTable.updateNumberOfGuests(numberOfGuests);

        //then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @Test
    @DisplayName("빈 상태가 true인 경우 예외를 던진다.")
    void validateIsEmptyWithTrue() {
        //given
        orderTable.updateEmpty(true);

        //when
        //then
        assertThatThrownBy(orderTable::validateIsEmpty)
                .isInstanceOf(IllegalArgumentException.class);
    }
}
