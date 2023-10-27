package kitchenpos.tablegroup.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

class TableGroupTest {

    @DisplayName("그룹으로 묶인 테이블 개수가 2개 미만이면, 생성할 수 없다.")
    @Test
    void createTableGroupFailTest_BySizeIsLessThanTwo() {
        //given
        OrderTable orderTable = OrderTable.create(0, Boolean.FALSE);

        //when then
        assertThatThrownBy(() -> GroupedTables.createForGrouping(List.of(orderTable)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹화 할 테이블 개수는 2 이상이어야 합니다");
    }

    @DisplayName("주문이 가능한 상태의 테이블이 존재하면, 그룹으로 생성할 수 없다.")
    @Test
    void createTableGroupFailTest_ByExistsNotEmptyTable() {
        //given
        OrderTable orderTable1 = OrderTable.create(0, Boolean.FALSE);
        OrderTable orderTable2 = OrderTable.create(0, Boolean.TRUE);

        //when then
        assertThatThrownBy(() -> GroupedTables.createForGrouping(List.of(orderTable1, orderTable2)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 가능한 상태의 테이블이 존재합니다.");
    }

    @DisplayName("주문 테이블로 묶인(그룹화 된) 테이블들은 주문 가능 상태(Not Empty)로 변한다.")
    @Test
    void createTableGroupSuccessTest_ChangeEmptyToFalse() {
        //given
        OrderTable orderTable1 = OrderTable.create(0, Boolean.TRUE);
        OrderTable orderTable2 = OrderTable.create(0, Boolean.TRUE);

        //when
        GroupedTables groupedTables = GroupedTables.createForGrouping(List.of(orderTable1, orderTable2));
        groupedTables.group(1L);

        //then
        assertThat(groupedTables.getOrderTables()).extractingResultOf("isEmpty")
                .containsExactly(Boolean.FALSE, Boolean.FALSE);
    }

}
