package kitchenpos.domain;

import static kitchenpos.TestObjectFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("주문 등록 불가 여부 변경")
    @Test
    void changeEmpty() {
        OrderTable orderTable = createOrderTable(true);

        orderTable.changeEmpty(false);

        assertThat(orderTable.isEmpty()).isFalse();
    }

    @DisplayName("[예외] 그룹에 포함된 테이블의 주문 등록 불가 여부 변경")
    @Test
    void changeEmpty_Fail_With_TableInGroup() {
        TableGroup tableGroup = createTableGroup();
        OrderTable orderTable = OrderTable.builder()
            .empty(true)
            .tableGroup(tableGroup)
            .build();

        assertThatThrownBy(() -> orderTable.changeEmpty(false))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 지정")
    @Test
    void groupBy() {
        OrderTable orderTable = createOrderTable(true);
        TableGroup tableGroup = createTableGroup();

        orderTable.groupBy(tableGroup);

        assertAll(
            () -> assertThat(orderTable.getTableGroup()).isEqualTo(tableGroup),
            () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("[예외] 주문 가능한 테이블의 그룹 설정")
    @Test
    void groupBy_Fail_With_NotEmptyTable() {
        OrderTable orderTable = createOrderTable(false);
        TableGroup tableGroup = createTableGroup();

        assertThatThrownBy(
            () -> orderTable.groupBy(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("[예외] 이미 다른 그룹에 속한 테이블의 그룹 설정")
    @Test
    void groupBy_Fail_With_AlreadyGroupedTable() {
        OrderTable orderTable = createOrderTable(true);
        TableGroup tableGroup = createTableGroup();
        orderTable.groupBy(tableGroup);

        assertThatThrownBy(
            () -> orderTable.groupBy(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 해제")
    @Test
    void ungroup() {
        OrderTable orderTable = createOrderTable(true);
        TableGroup tableGroup = createTableGroup();
        orderTable.groupBy(tableGroup);

        orderTable.ungroup();

        assertAll(
            () -> assertThat(orderTable.getTableGroup()).isNull(),
            () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("손님 수 변경")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = createOrderTable(false);

        orderTable.changeNumberOfGuests(10);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);
    }

    @DisplayName("[예외] 0보다 작은 수로 손님 수 변경")
    @Test
    void changeNumberOfGuests_Fail_With_InvalidNumberOfGuest() {
        OrderTable orderTable = createOrderTable(false);

        assertThatThrownBy(
            () -> orderTable.changeNumberOfGuests(-1)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 빈 테이블의 손님 수 변경")
    @Test
    void changeNumberOfGuests_Fail_With_EmptyTable() {
        OrderTable orderTable = createOrderTable(true);

        assertThatThrownBy(
            () -> orderTable.changeNumberOfGuests(10)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private TableGroup createTableGroup() {
        return TableGroup.builder()
            .createdDate(LocalDateTime.now())
            .build();
    }


}