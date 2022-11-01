package kitchenpos.domain;

import static kitchenpos.Fixture.DomainFixture.GUEST_NUMBER;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.exception.GroupTableNotEnoughException;
import kitchenpos.exception.GroupedTableNotEmptyException;
import kitchenpos.exception.TableAlreadyGroupedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTablesTest {

    @DisplayName("2보다 적은 테이블 수로 그룹테이블을 생성하려고 하면 예외를 발생시킨다.")
    @Test
    void from_Exception_NotEnoughTable() {
        OrderTable orderTable = new OrderTable(GUEST_NUMBER, true);

        assertThatThrownBy(() -> OrderTables.forGrouping(List.of(orderTable)))
                .isInstanceOf(GroupTableNotEnoughException.class);
    }

    @DisplayName("그룹으로 묶으려는 테이블들 중 하나라도 empty가 아닌 테이블이 있다면 예외를 발생시킨다.")
    @Test
    void from_Exception_NotEmptyTable() {
        OrderTable orderTable1 = new OrderTable(GUEST_NUMBER, true);
        OrderTable orderTable2 = new OrderTable(GUEST_NUMBER, false);

        assertThatThrownBy(() -> OrderTables.forGrouping(List.of(orderTable1, orderTable2)))
                .isInstanceOf(GroupedTableNotEmptyException.class);
    }

    @DisplayName("이미 테이블 그룹이 있는 테이블을 포함하여 테이블 그룹으로 묶으려고 하면 예외를 발생시킨다.")
    @Test
    void from_Exception_AlreadyGroupedTable() {
        OrderTable orderTable1 = new OrderTable(GUEST_NUMBER, true);
        OrderTable orderTable2 = new OrderTable(GUEST_NUMBER, true, new TableGroup());

        assertThatThrownBy(() -> OrderTables.forGrouping(List.of(orderTable1, orderTable2)))
                .isInstanceOf(TableAlreadyGroupedException.class);
    }
}