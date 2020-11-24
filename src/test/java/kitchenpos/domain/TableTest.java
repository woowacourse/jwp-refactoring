package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TableTest {

    @ParameterizedTest
    @CsvSource({"0,true", "1,false"})
    @DisplayName("constructor")
    void constructor(int numberOfGuests, boolean isEmpty) {
        assertThat(new Table(numberOfGuests, isEmpty))
            .isInstanceOf(Table.class);
    }

    @Test
    @DisplayName("constructor - 빈 테이블의 손님 수가 0보다 클 경우 예외처리")
    void constructor_IfGuestsCountOfEmptyTableAreLargerThanZero_ThrowException() {
        assertThatThrownBy(() -> new Table(1, true))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void putInGroup() {
        Table tableA = new Table(0, true);

        tableA.putInGroup(1L);

        assertThat(tableA.getTableGroupId()).isEqualTo(1L);
    }

    @Test
    void putInGroup_IfTableAlreadyBelongsGroup_ThrowException() {
        Table tableA = new Table(0, true);

        tableA.putInGroup(1L);
        assertThatThrownBy(() -> tableA.putInGroup(2L))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void putInGroup_IfTableIsNotEmpty_ThrowException() {
        Table tableA = new Table(1, false);

        assertThatThrownBy(() -> tableA.putInGroup(1L))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void isGrouped() {
        Table tableA = new Table(0, true);
        assertThat(tableA.isGrouped()).isFalse();

        tableA.putInGroup(1L);
        assertThat(tableA.isGrouped()).isTrue();
    }

    @Test
    void excludeFromGroup() {
        Table tableA = new Table(0, true);

        tableA.putInGroup(1L);
        assertThat(tableA.getTableGroupId()).isEqualTo(1L);

        tableA.excludeFromGroup();
        assertThat(tableA.getTableGroupId()).isEqualTo(null);
    }

    @ParameterizedTest
    @CsvSource({"true,true", "true,false", "false,true", "false,false"})
    void changeEmpty(boolean from, boolean to) {
        Table table = new Table(0, from);

        table.changeEmpty(to);
        assertThat(table.isEmpty()).isEqualTo(to);
    }

    @Test
    void changeNumberOfGuests() {
        Table table = new Table(0, false);

        table.changeNumberOfGuests(5);
        assertThat(table.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    void changeNumberOfGuests_IfTableIsEmpty_ThrowException() {
        Table table = new Table(0, true);

        assertThatThrownBy(() -> table.changeNumberOfGuests(5))
            .isInstanceOf(IllegalStateException.class);
    }
}
