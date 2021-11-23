package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import kitchenpos.fixtures.OrderFixtures;
import kitchenpos.fixtures.TableFixtures;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void 그룹을_지정한다() {
        OrderTable orderTable = TableFixtures.createOrderTable(true);
        TableGroup tableGroup = TableFixtures.createTableGroup();
        orderTable.belongsTo(tableGroup);

        assertThat(orderTable.getTableGroup()).isNotNull();
    }

    @Test
    void 그룹_지정_시_비어있지_않으면_예외를_반환한다() {
        OrderTable notEmptyTable = TableFixtures.createOrderTable(false);
        TableGroup tableGroup = TableFixtures.createTableGroup();

        Exception exception = assertThrows(IllegalStateException.class, () -> notEmptyTable.belongsTo(tableGroup));
        assertThat(exception.getMessage()).isEqualTo("빈 테이블이 아닙니다.");
    }

    @Test
    void 그룹_지정_시_이미_지정되어_있으면_예외를_반환한다() {
        OrderTable groupedTable = TableFixtures.createGroupedOrderTable(true);
        TableGroup tableGroup = TableFixtures.createTableGroup();

        Exception exception = assertThrows(IllegalStateException.class, () -> groupedTable.belongsTo(tableGroup));
        assertThat(exception.getMessage()).isEqualTo("이미 단체 지정된 테이블입니다.");
    }

    @Test
    void 그룹_지정을_해제한다() {
        OrderTable groupedTable = TableFixtures.createGroupedOrderTable(true);
        groupedTable.ungroup();

        assertThat(groupedTable.getTableGroup()).isNull();
    }

    @Test
    void 해제_시_완료되지_않은_주문이_있으면_예외를_반환한다() {
        OrderTable groupedTable = TableFixtures.createGroupedOrderTable(true);
        groupedTable.addOrder(OrderFixtures.createMealOrders().get(0));

        Exception exception = assertThrows(IllegalStateException.class, groupedTable::ungroup);
        assertThat(exception.getMessage()).isEqualTo("주문 상태가 완료되지 않았습니다.");
    }

    @Test
    void 테이블_상태를_변경한다() {
        OrderTable orderTable = TableFixtures.createOrderTable(true);
        orderTable.changeEmpty(false);

        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    void 상태_변경_시_단체_지정되어_있으면_예외를_반환한다() {
        OrderTable groupedTable = TableFixtures.createGroupedOrderTable(true);

        Exception exception = assertThrows(IllegalStateException.class, () -> groupedTable.changeEmpty(false));
        assertThat(exception.getMessage()).isEqualTo("이미 단체 지정된 테이블입니다.");
    }

    @Test
    void 상태_변경_시_완료되지_않은_주문이_있으면_예외를_반환한다() {
        OrderTable orderTable = TableFixtures.createOrderTable(true);
        orderTable.addOrder(OrderFixtures.createMealOrders().get(0));

        Exception exception = assertThrows(IllegalStateException.class, () -> orderTable.changeEmpty(false));
        assertThat(exception.getMessage()).isEqualTo("주문 상태가 완료되지 않았습니다.");
    }

    @Test
    void 손님_수를_변경한다() {
        OrderTable orderTable = TableFixtures.createOrderTable(false);
        int expected = orderTable.getNumberOfGuests() + 100;
        orderTable.changeNumberOfGuests(expected);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(expected);
    }

    @Test
    void 손님_수_변경_시_빈_테이블이면_예외를_반환한다() {
        OrderTable orderTable = TableFixtures.createOrderTable(true);

        Exception exception = assertThrows(IllegalStateException.class, () -> orderTable.changeNumberOfGuests(100));
        assertThat(exception.getMessage()).isEqualTo("빈 테이블입니다.");
    }

    @Test
    void 손님_수_변경_시_입력값이_음수이면_예외를_반환한다() {
        OrderTable orderTable = TableFixtures.createOrderTable(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderTable.changeNumberOfGuests(-1));
        assertThat(exception.getMessage()).isEqualTo("입력값이 유효하지 않습니다.");
    }
}