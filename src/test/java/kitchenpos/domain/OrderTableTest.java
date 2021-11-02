package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.factory.OrderTableFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = OrderTableFactory.builder()
            .id(1L)
            .tableGroupId(1L)
            .numberOfGuests(1)
            .empty(true)
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

    @DisplayName("그룹에 OrderTable 을 포함시킨다")
    @Test
    void joinGroup() {
        // given
        Long tableGroupId = 1L;

        // when
        orderTable.joinGroup(tableGroupId);

        // then
        assertThat(orderTable.getTableGroupId()).isEqualTo(1L);
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
