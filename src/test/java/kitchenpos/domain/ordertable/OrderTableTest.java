package kitchenpos.domain.ordertable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("테이블을 단체로 지정한다.")
    @Test
    void joinGroup() {
        final OrderTable orderTable = OrderTable.ofNew(0, true);
        final TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());
        orderTable.joinGroup(tableGroup);

        assertAll(
                () -> assertThat(orderTable.getTableGroupId()).isEqualTo(tableGroup.getId()),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("테이블을 단체에서 해제한다.")
    @Test
    void ungroup() {
        final OrderTable orderTable = OrderTable.ofNew(0, true);
        orderTable.ungroup();

        assertAll(
                () -> assertThat(orderTable.getTableGroupId()).isNull(),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("고객이 테이블을 이용한다.")
    @Test
    void acceptGuests() {
        final OrderTable orderTable = OrderTable.ofNew(0, true);
        orderTable.acceptGuests();

        assertThat(orderTable.isEmpty()).isFalse();
    }

    @DisplayName("특정한 수의 고객이 테이블을 이용한다.")
    @Test
    void acceptGuests_withNumberOfGuests() {
        final OrderTable orderTable = OrderTable.ofNew(0, false);
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
        final OrderTable orderTable = OrderTable.ofNew(2, false);
        orderTable.clear();

        assertThat(orderTable.isEmpty()).isTrue();
    }
}
