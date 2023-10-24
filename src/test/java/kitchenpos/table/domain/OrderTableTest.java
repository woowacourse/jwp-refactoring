package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("테이블에 방문한 손님 수는 0 이상이어야 한다.")
    @Test
    void createOrderTableFailTest_ByNumberOfGuestsIsLessThanZero() {
        assertThatThrownBy(() -> OrderTable.createWithoutTableGroup(-1, Boolean.TRUE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블에 방문한 손님 수는 0 이상이어야 합니다.");
    }

    @DisplayName("주문할 수 없는 테이블(Empty)이면, 방문 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsFailTest_ByEmpty() {
        //given
        OrderTable orderTable = OrderTable.createWithoutTableGroup(0, Boolean.TRUE);

        //when then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(1000))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문을 할 수 없는 상태이므로, 방문 손님 수를 변경할 수 없습니다.");
    }

    @DisplayName("그룹화 된 테이블이 존재하면, 주문 가능 상태를 변경할 수 없다.")
    @Test
    void changeEmptyFailTest_ByExistsGroupedTable() {
        //given
        OrderTable orderTable1 = OrderTable.createWithoutTableGroup(0, Boolean.TRUE);
        OrderTable orderTable2 = OrderTable.createWithoutTableGroup(0, Boolean.TRUE);
        TableGroup tableGroup = TableGroup.createWithGrouping(List.of(orderTable1, orderTable2));

        //when then
        assertThatThrownBy(() -> orderTable1.changeEmpty(Boolean.FALSE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("다른 그룹에 속해있으므로, 주문 상태를 변경할 수 없습니다.");
    }

    @DisplayName("주문할 수 있는 테이블(Not Empty)이면, 방문 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuestsSuccessTest() {
        //given
        OrderTable orderTable = OrderTable.createWithoutTableGroup(0, Boolean.FALSE);

        //when then
        assertDoesNotThrow(() -> orderTable.changeNumberOfGuests(1000));
    }

    @DisplayName("그룹화 된 테이블이 존재하지 않으면, 주문 가능 상태를 변경할 수 있다.")
    @Test
    void changeEmptySuccessTest() {
        //given
        OrderTable orderTable = OrderTable.createWithoutTableGroup(0, Boolean.FALSE);

        //when then
        assertDoesNotThrow(() -> orderTable.changeEmpty(Boolean.TRUE));
    }

}
