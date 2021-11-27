package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.table.exception.CannotChangeOrderTableEmpty;
import kitchenpos.table.exception.CannotChangeOrderTableGuest;
import kitchenpos.table.exception.InvalidTableGroupException;
import kitchenpos.table.exception.InvalidTableGroupSizeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("OrderTable과 TableGroup 관련 도메인 테스트")
class OrderTableTest {

    @DisplayName("[성공] 테이블 그룹에 포함될 수 있는 여부 확인")
    @Test
    void canCreateTableGroup_Success() {
        // given
        kitchenpos.table.domain.OrderTable orderTable = new kitchenpos.table.domain.OrderTable(0, true);

        // when
        boolean response = orderTable.canCreateTableGroup();

        // then
        Assertions.assertThat(response).isTrue();
    }

    @DisplayName("[성공] OrderTable의 비어있는 여부를 변경 가능")
    @Test
    void changeEmptyStatus_Success() {
        // given
        kitchenpos.table.domain.OrderTable orderTable = new kitchenpos.table.domain.OrderTable(0, true);

        // when
        orderTable.changeEmptyStatus(false);

        // then
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @DisplayName("[실패] OrderTable이 TableGroup에 속한 경우 비어있는 여부를 변경 불가")
    @Test
    void changeEmptyStatus_IncludeInTableGroup_ExceptionThrown() {
        // given
        kitchenpos.table.domain.OrderTable orderTable = new kitchenpos.table.domain.OrderTable(0, true);
        orderTable.includeInTableGroup(1L);

        // when
        // then
        Assertions.assertThatThrownBy(() -> orderTable.changeEmptyStatus(false))
            .isInstanceOf(CannotChangeOrderTableEmpty.class);
    }

    @DisplayName("[실패] 손님 수가 음수라면 변경 불가")
    @Test
    void changeNumberOfGuest_NegativeNumber_ExceptionThrown() {
        // given
        kitchenpos.table.domain.OrderTable orderTable = new kitchenpos.table.domain.OrderTable(0, true);
        orderTable.changeEmptyStatus(false);

        // when
        // then
        Assertions.assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-10))
            .isInstanceOf(CannotChangeOrderTableGuest.class);
    }

    @DisplayName("[실패] 현재 테이블이 비어있다면 손님 수 변경 불가")
    @Test
    void changeNumberOfGuest_EmptyTable_ExceptionThrown() {
        // given
        kitchenpos.table.domain.OrderTable orderTable = new kitchenpos.table.domain.OrderTable(0, true);

        // when
        // then
        Assertions.assertThatThrownBy(() -> orderTable.changeNumberOfGuests(5))
            .isInstanceOf(CannotChangeOrderTableGuest.class);
    }

    @DisplayName("[실패] OrderTables의 수가 2보다 작다면 TableGroup 생성 불가")
    @Test
    void tableGroup_LessThan2Tables_ExceptionThrown() {
        // given
        List<kitchenpos.table.domain.OrderTable> orderTables = Collections.singletonList(defaultOrderTable());

        // when
        // then
        Assertions.assertThatThrownBy(() -> new kitchenpos.table.domain.TableGroup(orderTables))
            .isInstanceOf(InvalidTableGroupSizeException.class);
    }

    @DisplayName("[실패] OrderTables중 비어있지 않은 테이블이 있다면 TableGroup 생성 불가")
    @Test
    void tableGroup_NotEmptyTable_ExceptionThrown() {
        // given
        kitchenpos.table.domain.OrderTable notEmptyTable = new kitchenpos.table.domain.OrderTable(2, false);
        List<kitchenpos.table.domain.OrderTable> orderTables = Arrays.asList(defaultOrderTable(), notEmptyTable);

        // when
        // then
        Assertions.assertThatThrownBy(() -> new kitchenpos.table.domain.TableGroup(orderTables))
            .isInstanceOf(InvalidTableGroupException.class);
    }

    @DisplayName("[실패] OrderTables중 TableGroup에 이미 속한 테이블이 있다면 TableGroup 생성 불가")
    @Test
    void tableGroup_TableGroupNotNull_ExceptionThrown() {
        // given
        kitchenpos.table.domain.OrderTable includeInTableGroup = new kitchenpos.table.domain.OrderTable(0, true);
        includeInTableGroup.includeInTableGroup(1L);
        List<kitchenpos.table.domain.OrderTable> orderTables = Arrays.asList(defaultOrderTable(), includeInTableGroup);

        // when
        // then
        Assertions.assertThatThrownBy(() -> new kitchenpos.table.domain.TableGroup(orderTables))
            .isInstanceOf(InvalidTableGroupException.class);
    }

    private kitchenpos.table.domain.OrderTable defaultOrderTable() {
        return new kitchenpos.table.domain.OrderTable(0, true);
    }
}
