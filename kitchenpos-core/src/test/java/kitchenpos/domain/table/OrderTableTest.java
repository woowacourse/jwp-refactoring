package kitchenpos.domain.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.common.OrderStatus;
import kitchenpos.exception.badrequest.CookingOrMealOrderTableCannotChangeEmptyException;
import kitchenpos.exception.badrequest.CookingOrMealOrderTableCannotUngroupedException;
import kitchenpos.exception.badrequest.EmptyTableCannotChangeNumberOfGuestsException;
import kitchenpos.exception.badrequest.GroupedTableCannotChangeEmptyException;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void 단체_지정이_되어_있으면_빈_테이블_여부를_변경할_수_없다() {
        OrderTable orderTable1 = new OrderTable(1, true);
        OrderTable orderTable2 = new OrderTable(1, true);
        new TableGroup(List.of(orderTable1, orderTable2));

        assertThatThrownBy(() -> orderTable1.changeEmpty(true))
                .isInstanceOf(GroupedTableCannotChangeEmptyException.class);
    }

    @Test
    void 빈_테이블_여부를_변경할_때_조리_중인_주문이_있을_경우_예외를_반환한다() {
        OrderTable orderTable = new OrderTable(1, false);
        new OrderStatusRecord(1L, orderTable, OrderStatus.COOKING);

        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(CookingOrMealOrderTableCannotChangeEmptyException.class);
    }

    @Test
    void 빈_테이블_여부를_변경할_때_식사_중인_주문이_있을_경우_예외를_반환한다() {
        OrderTable orderTable = new OrderTable(1, false);
        new OrderStatusRecord(1L, orderTable, OrderStatus.MEAL);

        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(CookingOrMealOrderTableCannotChangeEmptyException.class);
    }

    @Test
    void 빈_테이블의_인원_수를_변경할_수_없다() {
        OrderTable orderTable = new OrderTable(0, true);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(1))
                .isInstanceOf(EmptyTableCannotChangeNumberOfGuestsException.class);
    }

    @Test
    void 그룹_지정을_해제한다() {
        OrderTable orderTable1 = new OrderTable(1, true);
        OrderTable orderTable2 = new OrderTable(1, true);
        new TableGroup(List.of(orderTable1, orderTable2));

        orderTable1.ungroup();

        assertAll(
                () -> assertThat(orderTable1.getTableGroup()).isNull(),
                () -> assertThat(orderTable1.isEmpty()).isFalse()
        );
    }

    @Test
    void 그룹_지정을_해제할_때_조리_중인_주문이_있을_경우_예외를_반환한다() {
        OrderTable orderTable1 = new OrderTable(1, true);
        OrderTable orderTable2 = new OrderTable(1, true);
        new TableGroup(List.of(orderTable1, orderTable2));
        new OrderStatusRecord(1L, orderTable1, OrderStatus.COOKING);

        assertThatThrownBy(orderTable1::ungroup).isInstanceOf(CookingOrMealOrderTableCannotUngroupedException.class);
    }

    @Test
    void 그룹_지정을_해제할_때_식사_중인_주문이_있을_경우_예외를_반환한다() {
        OrderTable orderTable1 = new OrderTable(1, true);
        OrderTable orderTable2 = new OrderTable(1, true);
        new TableGroup(List.of(orderTable1, orderTable2));
        new OrderStatusRecord(1L, orderTable1, OrderStatus.MEAL);

        assertThatThrownBy(orderTable1::ungroup).isInstanceOf(CookingOrMealOrderTableCannotUngroupedException.class);
    }
}
