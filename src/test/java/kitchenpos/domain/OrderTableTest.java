package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class OrderTableTest {

    @DisplayName("주문 테이블의 빈 상태를 변경한다.")
    @Test
    void updateEmpty() {
        // given
        final OrderTable orderTable = new OrderTable(3, true);

        // when
        orderTable.updateEmpty(false);

        // then
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블이 그룹에 포함되어 있다면 빈 상태 변경 시 예외가 발생한다.")
    @Test
    void updateEmpty_fail() {
        // given
        final OrderTable orderTable = new OrderTable(3, true);
        orderTable.groupBy(TableGroup.saved(1L, LocalDateTime.now()));

        // when, then
        assertThatThrownBy(() -> orderTable.updateEmpty(false))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("그룹에 속해있는 테이블은 빈 상태를 변경할 수 없습니다.");
    }

    @DisplayName("주문 테이블을 그룹 해제한다.")
    @Test
    void ungroup() {
        // given
        final OrderTable orderTable = new OrderTable(3, true);
        orderTable.groupBy(new TableGroup(LocalDateTime.now()));

        // when1
        orderTable.ungroup();

        // then
        assertThat(orderTable.getTableGroupId()).isNull();
    }

    @DisplayName("주문 테이블을 그룹으로 묶는다.")
    @Test
    void groupBy() {
        // given
        final OrderTable orderTable = new OrderTable(3, true);

        // when
        orderTable.groupBy(TableGroup.saved(1L, LocalDateTime.now()));

        // then
        assertThat(orderTable.getTableGroupId()).isNotNull();
    }

    @DisplayName("그룹으로 묶을 수 없는 테이블이면 true 가 반환된다.")
    @ParameterizedTest
    @MethodSource("createUnableToGroupTables")
    void isUnableToBeGrouped(final OrderTable orderTable) {
        // given
        // when
        final boolean isUnableToGroup = orderTable.isUnableToBeGrouped();

        // then
        assertThat(isUnableToGroup).isTrue();
    }

    private static Stream<OrderTable> createUnableToGroupTables() {
        return Stream.of(
            new OrderTable(3, false),
           createGroupedOrderTable()
        );
    }

    private static OrderTable createGroupedOrderTable() {
        final OrderTable orderTable = new OrderTable(3, true);
        orderTable.groupBy(TableGroup.saved(1L, LocalDateTime.now()));

        return orderTable;
    }

    @DisplayName("그룹으로 묶을 수 없는 테이블이면 false 가 반환된다.")
    @Test
    void isUnableToBeGrouped_false() {
        // given
        final OrderTable orderTable = new OrderTable(3, true);

        // when
        final boolean isUnableToGroup = orderTable.isUnableToBeGrouped();

        // then
        assertThat(isUnableToGroup).isFalse();
    }

    @DisplayName("테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTable orderTable = new OrderTable(3, false);

        // when
        orderTable.changeNumberOfGuests(4);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(4);
    }

    @DisplayName("테이블이 비어있다면 손님 수 변경 시 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_fail() {
        // given
        final OrderTable orderTable = new OrderTable(3, true);

        // when, then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(4))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("비어있는 테이블은 손님 수를 바꿀 수 없습니다.");
    }
}
