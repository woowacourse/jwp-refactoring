package kitchenpos.domain;

import static kitchenpos.exception.TableGroupExceptionType.ILLEGAL_ADD_ORDER_TABLE_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.exception.BaseExceptionType;
import kitchenpos.exception.TableGroupException;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    void 새로운_OrderTable을_더한다() {
        // given
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        OrderTable orderTable = new OrderTable(null, 10, true);

        // when
        tableGroup.add(orderTable);

        // then
        assertThat(tableGroup.orderTables()).contains(orderTable);
    }

    @Test
    void OrderTable을_추가할_때_OrderTable이_Empty_상태가_아니면_예외를_발생한다() {
        // given
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        OrderTable orderTable = new OrderTable(null, 10, false);

        // when
        BaseExceptionType exceptionType = assertThrows(TableGroupException.class, () ->
                tableGroup.add(orderTable)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(ILLEGAL_ADD_ORDER_TABLE_EXCEPTION);
    }

    @Test
    void OrderTable을_추가할_때_OrderTable이_이미_TableGroup을_가지면_예외를_발생한다() {
        // given
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        OrderTable orderTable = new OrderTable(tableGroup, 1, false);

        // when
        BaseExceptionType exceptionType = assertThrows(TableGroupException.class, () ->
                tableGroup.add(orderTable)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(ILLEGAL_ADD_ORDER_TABLE_EXCEPTION);
    }

    @Test
    void 여러_OrderTable을_한꺼번에_저장한다() {
        // given
        OrderTable orderTable1 = new OrderTable(null, 10, true);
        OrderTable orderTable2 = new OrderTable(null, 10, true);
        List<OrderTable> orderTables = List.of(orderTable1, orderTable2);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        // when
        tableGroup.add(orderTables);

        // then
        assertThat(tableGroup.orderTables()).containsAll(orderTables);
    }

    @Test
    void 생성자를_통해_여러_OrderTable을_한번에_저장한다() {
        // given
        OrderTable orderTable1 = new OrderTable(null, 10, true);
        OrderTable orderTable2 = new OrderTable(null, 10, true);
        List<OrderTable> orderTables = List.of(orderTable1, orderTable2);

        // when
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);

        // then
        assertThat(tableGroup.orderTables()).containsAll(orderTables);
    }
}
