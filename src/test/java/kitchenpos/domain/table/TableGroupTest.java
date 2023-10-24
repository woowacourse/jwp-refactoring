package kitchenpos.domain.table;

import static kitchenpos.exception.table.TableGroupExceptionType.CAN_NOT_UNGROUP_COOKING_OR_MEAL;
import static kitchenpos.exception.table.TableGroupExceptionType.ORDER_TABLES_CAN_NOT_LESS_THAN_TWO;
import static kitchenpos.exception.table.TableGroupExceptionType.ORDER_TABLE_CAN_NOT_EMPTY;
import static kitchenpos.exception.table.TableGroupExceptionType.ORDER_TABLE_CAN_NOT_HAVE_TABLE_GROUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import kitchenpos.domain.order.OrderFixture;
import kitchenpos.exception.BaseException;
import kitchenpos.exception.BaseExceptionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    void 주문테이블이_null이면_예외가_발생한다() {
        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                new TableGroup(null)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(ORDER_TABLES_CAN_NOT_LESS_THAN_TWO);
    }

    @Test
    void 주문테이블이_비어있으면_예외가_발생한다() {
        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                new TableGroup(List.of())
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(ORDER_TABLES_CAN_NOT_LESS_THAN_TWO);
    }

    @Test
    void 주문테이블이_하나면_예외가_발생한다() {
        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                new TableGroup(List.of(new OrderTable()))
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(ORDER_TABLES_CAN_NOT_LESS_THAN_TWO);
    }

    @Test
    void 주문테이블이_비어있지_않으면_예외가_발생한다() {
        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                new TableGroup(List.of(new OrderTable(0, false), new OrderTable(0, false)))
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(ORDER_TABLE_CAN_NOT_EMPTY);
    }

    @Test
    void 지정된_그룹이_있는_주문테이블이_있으면_예외가_발생한다() {
        // given
        OrderTable 지정된_그룹이_있는_테이블 = new OrderTable(null, new TableGroup(), 0, true);
        OrderTable 주문테이블 = new OrderTable(0, true);

        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                new TableGroup(List.of(지정된_그룹이_있는_테이블, 주문테이블))
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(ORDER_TABLE_CAN_NOT_HAVE_TABLE_GROUP);
    }

    @Test
    void 지정된_그룹이_없고_비어있지않은_주문테이블이_2개이상있으면_예외가_발생하지_않는다() {
        // given
        OrderTable 주문테이블1 = new OrderTable(0, true);
        OrderTable 주문테이블2 = new OrderTable(0, true);

        // when & then
        assertThatCode(() -> new TableGroup(List.of(주문테이블1, 주문테이블2)))
                .doesNotThrowAnyException();
    }

    @Test
    void 조리중이거나_식사중인_주문테이블이_있으면_예외가_발생한다() {
        // given
        OrderTable 조리중인_주문테이블 = new OrderTable(null, null, List.of(OrderFixture.조리중인_주문()), 0, true);
        OrderTable 계산완료된_주문테이블 = new OrderTable(null, null, List.of(OrderFixture.계산완료된_주문()), 0, true);
        TableGroup tableGroup = new TableGroup(List.of(조리중인_주문테이블, 계산완료된_주문테이블));

        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                tableGroup.ungroup()
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(CAN_NOT_UNGROUP_COOKING_OR_MEAL);
    }

    @Test
    void 그룹을_해제한다() {
        // given
        OrderTable 계산완료된_주문테이블1 = new OrderTable(null, null, List.of(OrderFixture.계산완료된_주문()), 0, true);
        OrderTable 계산완료된_주문테이블2 = new OrderTable(null, null, List.of(OrderFixture.계산완료된_주문()), 0, true);
        TableGroup tableGroup = new TableGroup(List.of(계산완료된_주문테이블1, 계산완료된_주문테이블2));

        // when
        tableGroup.ungroup();

        // then
        Assertions.assertAll(
                () -> assertThat(tableGroup.orderTables().get(0).tableGroup()).isNull(),
                () -> assertThat(tableGroup.orderTables().get(0).empty()).isFalse(),
                () -> assertThat(tableGroup.orderTables().get(1).tableGroup()).isNull(),
                () -> assertThat(tableGroup.orderTables().get(1).empty()).isFalse()
        );
    }
}
