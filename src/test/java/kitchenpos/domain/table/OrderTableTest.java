package kitchenpos.domain.table;

import static kitchenpos.domain.order.OrderFixture.계산완료된_주문;
import static kitchenpos.domain.order.OrderFixture.조리중인_주문;
import static kitchenpos.exception.table.OrderTableExceptionType.CAN_NOT_CHANGE_EMPTY_COOKING_OR_MEAL;
import static kitchenpos.exception.table.OrderTableExceptionType.CAN_NOT_CHANGE_EMPTY_GROUPED_ORDER_TABLE;
import static kitchenpos.exception.table.OrderTableExceptionType.CAN_NOT_CHANGE_NUMBER_OF_GUESTS_EMPTY_ORDER_TABLE;
import static kitchenpos.exception.table.OrderTableExceptionType.NUMBER_OF_GUESTS_CAN_NOT_NEGATIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.exception.BaseException;
import kitchenpos.exception.BaseExceptionType;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void 조리중이거나_식사중인_주문이_있으면_true를_반환한다() {
        // given
        Order 조리중인_주문 = 조리중인_주문();
        Order 계산완료된_주문 = 계산완료된_주문();
        OrderTable orderTable = new OrderTable(null, null, List.of(조리중인_주문, 계산완료된_주문), 0, false);

        // when
        boolean result = orderTable.hasOrderOfCookingOrMeal();

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 조리중이거나_식사중인_주문이_하나도_없으면_false를_반환한다() {
        // given
        Order 계산완료된_주문1 = 계산완료된_주문();
        Order 계산완료된_주문2 = 계산완료된_주문();
        OrderTable orderTable = new OrderTable(null, null, List.of(계산완료된_주문1, 계산완료된_주문2), 0, false);

        // when
        boolean result = orderTable.hasOrderOfCookingOrMeal();

        // then
        assertThat(result).isFalse();
    }

    @Test
    void 그룹을_해제하면_테이블_그룹은_null이_되고_비어있는지_여부는_false가_된다() {
        // given
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable = new OrderTable(null, tableGroup, null, 0, true);

        // when
        orderTable.ungroup();

        // then
        assertThat(orderTable.tableGroup()).isNull();
        assertThat(orderTable.empty()).isFalse();
    }

    @Test
    void 지정된_그룹이_존재하면_비어있는_상태를_변경할때_예외가_발생한다() {
        // given
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable = new OrderTable(null, tableGroup, null, 0, true);

        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                orderTable.changeEmpty(false)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(CAN_NOT_CHANGE_EMPTY_GROUPED_ORDER_TABLE);
    }

    @Test
    void 조리중이거나_식사중인_주문이_있으면_비어있는_상태를_변경할때_예외가_발생한다() {
        // given
        Order 조리중인_주문 = 조리중인_주문();
        Order 계산완료된_주문 = 계산완료된_주문();
        OrderTable orderTable = new OrderTable(null, null, List.of(조리중인_주문, 계산완료된_주문), 0, false);

        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                orderTable.changeEmpty(true)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(CAN_NOT_CHANGE_EMPTY_COOKING_OR_MEAL);
    }

    @Test
    void 비어있는_상태를_변경한다() {
        // given
        Order 계산완료된_주문1 = 계산완료된_주문();
        Order 계산완료된_주문2 = 계산완료된_주문();
        OrderTable orderTable = new OrderTable(null, null, List.of(계산완료된_주문1, 계산완료된_주문2), 0, false);

        // when
        orderTable.changeEmpty(true);

        // then
        assertThat(orderTable.empty()).isTrue();
    }

    @Test
    void 손님_숫자를_음수로_변경할때_예외가_발생한다() {
        // given
        OrderTable orderTable = OrderTable.emptyTable();

        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                orderTable.changeNumberOfGuests(-1)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(NUMBER_OF_GUESTS_CAN_NOT_NEGATIVE);
    }

    @Test
    void 비어있는_테이블의_손님_숫자를_변경할때_예외가_발생한다() {
        // given
        OrderTable orderTable = OrderTable.emptyTable();

        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                orderTable.changeNumberOfGuests(1)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(CAN_NOT_CHANGE_NUMBER_OF_GUESTS_EMPTY_ORDER_TABLE);
    }

    @Test
    void 손님_숫자를_변경한다() {
        // given
        OrderTable orderTable = new OrderTable(0, false);

        // when
        orderTable.changeNumberOfGuests(1);

        // then
        assertThat(orderTable.numberOfGuests()).isEqualTo(1);
    }
}
