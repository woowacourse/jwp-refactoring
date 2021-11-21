package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.factory.OrderTableFactory;
import kitchenpos.factory.TableGroupFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = OrderTableFactory.builder()
            .id(1L)
            .numberOfGuests(1)
            .empty(false)
            .build();
    }

    @DisplayName("OrderTable 의 empty 정보를 바꾼다")
    @Test
    void changeEmpty() {
        // given
        boolean changedEmpty = false;

        // when
        orderTable.changeEmpty(changedEmpty);

        // then
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @DisplayName("OrderTable 의 numberOfGuests 정보를 바꾼다")
    @Test
    void changeNumberOfGuests() {
        // given
        int changedNumberOfGuests = 2;

        // when
        orderTable.changeNumberOfGuests(changedNumberOfGuests);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(changedNumberOfGuests);
    }

    @DisplayName("OrderTable 의 numberOfGuests 정보 변경 실패 - 변경할 손님의 수가 음수인 경우")
    @Test
    void changeNumberOfGuestsFail_whenNumberOfGuestsIsNegative() {
        // given
        int changedNumberOfGuests = -1;

        // when
        ThrowingCallable throwingCallable =
            () -> orderTable.changeNumberOfGuests(changedNumberOfGuests);

        // then
        assertThatThrownBy(throwingCallable)
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderTable 의 numberOfGuests 정보 변경 실패 - 테이블이 비어있는 경우")
    @Test
    void changeNumberOfGuestsFail_whenOrderTableIsNotEmpty() {
        // given
        orderTable = OrderTableFactory.copy(orderTable)
            .empty(true)
            .build();
        int changedNumberOfGuests = 2;

        // when
        ThrowingCallable throwingCallable =
            () -> orderTable.changeNumberOfGuests(changedNumberOfGuests);

        // then
        assertThatThrownBy(throwingCallable)
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹에 OrderTable 을 포함시킨다")
    @Test
    void joinGroup() {
        // given
        Long tableGroupId = 1L;
        TableGroup tableGroup = TableGroupFactory.builder()
            .id(tableGroupId)
            .build();

        // when
        orderTable.joinGroup(tableGroup);

        // then
        assertThat(orderTable.getTableGroupId()).isEqualTo(tableGroupId);
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @DisplayName("그룹에서 OrderTable 을 제외시킨다")
    @Test
    void ungroup() {
        // when
        orderTable.ungroup();

        // then
        assertThat(orderTable.getTableGroupId()).isNull();
        assertThat(orderTable.isEmpty()).isFalse();
    }
}
