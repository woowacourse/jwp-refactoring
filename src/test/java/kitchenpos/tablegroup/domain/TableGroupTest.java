package kitchenpos.tablegroup.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @DisplayName("그룹으로 묶인 테이블 개수가 2개 미만이면, 생성할 수 없다.")
    @Test
    void createTableGroupFailTest_BySizeIsLessThanTwo() {
        //given
        OrderTable orderTable = OrderTable.createWithoutTableGroup(0, Boolean.FALSE);

        //when then
        assertThatThrownBy(() -> TableGroup.createWithGrouping(List.of(orderTable)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹화 할 테이블 개수는 2 이상이어야 합니다");
    }

    @DisplayName("주문이 가능한 상태의 테이블이 존재하면, 그룹으로 생성할 수 없다.")
    @Test
    void createTableGroupFailTest_ByExistsNotEmptyTable() {
        //given
        OrderTable orderTable1 = OrderTable.createWithoutTableGroup(0, Boolean.FALSE);
        OrderTable orderTable2 = OrderTable.createWithoutTableGroup(0, Boolean.TRUE);

        //when then
        assertThatThrownBy(() -> TableGroup.createWithGrouping(List.of(orderTable1, orderTable2)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 가능한 상태의 테이블이 존재합니다.");
    }

    @DisplayName("주문 테이블로 묶인(그룹화 된) 테이블들은 주문 가능 상태(Not Empty)로 변한다.")
    @Test
    void createTableGroupSuccessTest_ChangeEmptyToFalse() {
        //given
        OrderTable orderTable1 = OrderTable.createWithoutTableGroup(0, Boolean.TRUE);
        OrderTable orderTable2 = OrderTable.createWithoutTableGroup(0, Boolean.TRUE);

        //when
        TableGroup tableGroup = TableGroup.createWithGrouping(List.of(orderTable1, orderTable2));

        //then
        assertThat(tableGroup.getOrderTables()).extractingResultOf("isEmpty")
                .containsExactly(Boolean.FALSE, Boolean.FALSE);
    }

}
