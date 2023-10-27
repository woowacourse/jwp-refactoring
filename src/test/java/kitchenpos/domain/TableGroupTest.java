package kitchenpos.domain;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.tablegroup.TableGroup;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    void 단체_지정을_한다() {
        // given
        OrderTable orderTableA = new OrderTable(true);
        OrderTable orderTableB = new OrderTable(true);

        // when, then
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(of(orderTableA, orderTableB));

        // then
        assertThat(tableGroup.getOrderTables()).contains(orderTableA, orderTableB);
        assertThat(tableGroup.getOrderTables().get(0).isEmpty()).isFalse();
        assertThat(tableGroup.getOrderTables().get(1).isEmpty()).isFalse();
    }


    @Test
    void 주문_테이블이_1개_인경우_예외가_발생한다() {
        // given
        OrderTable orderTableA = new OrderTable(true);

        // when, then
        TableGroup tableGroup = new TableGroup();
        assertThatThrownBy(() -> tableGroup.setOrderTables(of(orderTableA)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_null인_경우_예외가_발생한다() {
        // given
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(null);
        orderTables.add(new OrderTable(true));

        // when, then
        TableGroup tableGroup = new TableGroup();
        assertThatThrownBy(() -> tableGroup.setOrderTables(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_비어있지_않는_경우_예외가_발생한다() {
        // given
        OrderTable orderTableA = new OrderTable(false);
        OrderTable orderTableB = new OrderTable(true);

        // when, then
        TableGroup tableGroup = new TableGroup();
        assertThatThrownBy(() -> tableGroup.setOrderTables(of(orderTableA, orderTableB)))
                .isInstanceOf(IllegalArgumentException.class);
    }

        @Test
        void 이미_단체_지정이_등록이_되어있는_경우_예외가_발생한다() {
            // given
            TableGroup tableGroup = new TableGroup();

            OrderTable orderTableA = new OrderTable(tableGroup,0,true);
            OrderTable orderTableB = new OrderTable(true);

            // when, then
            TableGroup newTableGroup = new TableGroup();
            assertThatThrownBy(() -> newTableGroup.setOrderTables(of(orderTableA, orderTableB)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

}
