package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import kitchenpos.tablegroup.domain.GroupedTables;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTableTest {

    @Mock
    private OrderTableValidator orderTableValidator;

    @DisplayName("테이블에 방문한 손님 수는 0 이상이어야 한다.")
    @Test
    void createOrderTableFailTest_ByNumberOfGuestsIsLessThanZero() {
        assertThatThrownBy(() -> OrderTable.create(-1, Boolean.TRUE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블에 방문한 손님 수는 0 이상이어야 합니다.");
    }

    @DisplayName("주문할 수 없는 테이블(Empty)이면, 방문 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsFailTest_ByEmpty() {
        //given
        OrderTable orderTable = OrderTable.create(0, Boolean.TRUE);

        //when then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(1000))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문을 할 수 없는 상태이므로, 방문 손님 수를 변경할 수 없습니다.");
    }

    @DisplayName("그룹화 된 테이블이 존재하면, 주문 가능 상태를 변경할 수 없다.")
    @Test
    void changeEmptyFailTest_ByExistsGroupedTable() {
        //given
        OrderTable orderTable1 = OrderTable.create(0, Boolean.TRUE);
        OrderTable orderTable2 = OrderTable.create(0, Boolean.TRUE);
        GroupedTables groupedTables = GroupedTables.createForGrouping(List.of(orderTable1, orderTable2));
        groupedTables.group(1L);

        //when then
        assertThatThrownBy(() -> orderTable1.changeEmpty(Boolean.FALSE, orderTableValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("다른 그룹에 속해있으므로, 주문 상태를 변경할 수 없습니다.");
    }

    @DisplayName("주문할 수 있는 테이블(Not Empty)이면, 방문 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuestsSuccessTest() {
        //given
        OrderTable orderTable = OrderTable.create(0, Boolean.FALSE);

        //when then
        assertDoesNotThrow(() -> orderTable.changeNumberOfGuests(1000));
    }

    @DisplayName("그룹화 된 테이블이 존재하지 않으면, 주문 가능 상태를 변경할 수 있다.")
    @Test
    void changeEmptySuccessTest() {
        //given
        OrderTable orderTable = OrderTable.create(0, Boolean.FALSE);

        //when then
        assertDoesNotThrow(() -> orderTable.changeEmpty(Boolean.TRUE, orderTableValidator));
    }

}
