package kitchenpos.domain.ordertable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OrderTableTest {

    private final static TableGroup TABLE_GROUP = mock(TableGroup.class);

    @BeforeAll
    static void setUp() {
        when(TABLE_GROUP.getId())
                .thenReturn(1L);
    }

    @DisplayName("테이블을 단체로 지정한다.")
    @Test
    void joinGroup() {
        // given, when
        final OrderTable orderTable = OrderTable.ofUnsaved(0, true);
        orderTable.joinGroup(TABLE_GROUP);

        // then
        assertAll(
                () -> assertThat(orderTable.getTableGroupId().get()).isEqualTo(1L),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("비어있지 않은 테이블을 단체로 지정하면 예외가 발생한다.")
    @Test
    void joinGroup_throwsException_ifNotEmpty() {
        // given
        final OrderTable notEmptyTable = OrderTable.ofUnsaved(0, false);

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> notEmptyTable.joinGroup(TABLE_GROUP));
    }

    @DisplayName("이미 단체로 지정된 테이블을 단체로 지정하면 예외가 발생한다.")
    @Test
    void joinGroup_throwsException_ifAlreadyGrouped() {
        // given
        final OrderTable groupedTable = OrderTable.ofUnsaved(0, true);
        groupedTable.joinGroup(TABLE_GROUP);

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> groupedTable.joinGroup(TABLE_GROUP));
    }

    @DisplayName("테이블을 단체에서 해제한다.")
    @Test
    void ungroup() {
        final OrderTable orderTable = OrderTable.ofUnsaved(0, true);
        orderTable.ungroup();

        assertAll(
                () -> assertThat(orderTable.getTableGroupId()).isEmpty(),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("고객이 테이블을 이용한다.")
    @Test
    void acceptGuests() {
        final OrderTable orderTable = OrderTable.ofUnsaved(0, true);
        orderTable.acceptGuests();

        assertThat(orderTable.isEmpty()).isFalse();
    }

    @DisplayName("고객이 단체로 지정된 테이블을 이용하면 예외가 발생한다.")
    @Test
    void acceptGuests_throwsException_ifGrouped() {
        // given
        final OrderTable groupedTable = OrderTable.ofUnsaved(0, true);
        groupedTable.joinGroup(TABLE_GROUP);

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> groupedTable.acceptGuests());
    }

    @DisplayName("특정한 수의 고객이 테이블을 이용한다.")
    @Test
    void acceptGuests_withNumberOfGuests() {
        // given
        final OrderTable orderTable = OrderTable.ofUnsaved(0, false);
        final int numberOfGuests = 2;

        // when
        orderTable.acceptGuests(numberOfGuests);

        // then
        assertAll(
                () -> assertThat(orderTable.isEmpty()).isFalse(),
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests)
        );
    }

    @DisplayName("테이블을 이용하는 고객 수가 0보다 작으면 예외가 발생한다.")
    @ValueSource(ints = {-1, -2, -3})
    @ParameterizedTest(name = "테이블을 이용하는 고객 수가 0보다 작은 {0}이면 예외가 발생한다.")
    void acceptGuests_withNumberOfGuests_throwsException_ifNumberUnder0(final int numberOfGuests) {
        // given
        final OrderTable orderTable = OrderTable.ofUnsaved(0, false);

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderTable.acceptGuests(numberOfGuests));
    }

    @DisplayName("테이블을 비운다.")
    @Test
    void clear() {
        final OrderTable orderTable = OrderTable.ofUnsaved(2, false);
        orderTable.clear();

        assertThat(orderTable.isEmpty()).isTrue();
    }
}
