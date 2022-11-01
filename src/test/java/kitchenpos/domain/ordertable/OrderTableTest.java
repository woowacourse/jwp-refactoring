package kitchenpos.domain.ordertable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("테이블을 단체로 지정한다.")
    @Test
    void joinGroup() {
        // given
        final TableGroup tableGroup = mock(TableGroup.class);
        when(tableGroup.getId())
                .thenReturn(1L);
        final OrderTable orderTable = OrderTable.ofUnsaved(0, true);

        // when
        orderTable.joinGroup(tableGroup);

        // then
        assertAll(
                () -> assertThat(orderTable.getTableGroupId().get()).isEqualTo(1L),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
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

    @DisplayName("특정한 수의 고객이 테이블을 이용한다.")
    @Test
    void acceptGuests_withNumberOfGuests() {
        final OrderTable orderTable = OrderTable.ofUnsaved(0, false);
        final int numberOfGuests = 2;
        orderTable.acceptGuests(numberOfGuests);

        assertAll(
                () -> assertThat(orderTable.isEmpty()).isFalse(),
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests)
        );
    }

    @DisplayName("테이블을 비운다.")
    @Test
    void clear() {
        final OrderTable orderTable = OrderTable.ofUnsaved(2, false);
        orderTable.clear();

        assertThat(orderTable.isEmpty()).isTrue();
    }
}
