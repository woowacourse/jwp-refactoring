package kitchenpos.domain;

import kitchenpos.exception.orderTableException.IllegalOrderTableGuestNumberException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class OrderTableTest {

    @Test
    void create() {
        //when&then
        assertDoesNotThrow(() -> new OrderTable(null, 3, false));
    }

    @Test
    void validGutestNumber() {
        //when&then
        assertThatThrownBy(() -> new OrderTable(null, -1, false))
                .isInstanceOf(IllegalOrderTableGuestNumberException.class)
                .hasMessage("잘못된 주문 테이블 게스트 숫자 설정입니다.");
    }

    @Test
    void changeEmptyStatus() {
        //given
        final OrderTable orderTable = new OrderTable(null, 3, false);

        //when
        orderTable.changeEmptyStatus(true);

        //when&then
        Assertions.assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    void changeTableGroup() {
        //given
        final OrderTable orderTable = new OrderTable(null, 3, false);
        final OrderTable orderTable2 = new OrderTable(null, 3, false);
        final TableGroup tableGroup = new TableGroup(new OrderTables(List.of(orderTable, orderTable2)));

        //when
        orderTable.changeTableGroup(tableGroup);

        //then
        assertAll(
                () -> assertThat(orderTable.getTableGroup()).isEqualTo(tableGroup),
                () -> assertThat(orderTable2.getTableGroup()).isEqualTo(tableGroup)
        );
    }

    @Test
    void ungroup() {
        //given
        final OrderTable orderTable = new OrderTable(null, 3, false);
        final OrderTable orderTable2 = new OrderTable(null, 3, false);
        final TableGroup tableGroup = new TableGroup(new OrderTables(List.of(orderTable, orderTable2)));
        orderTable.changeTableGroup(tableGroup);

        //when
        orderTable.ungroup();
        orderTable2.ungroup();

        //then
        assertAll(
                () -> assertThat(orderTable.getTableGroup()).isNull(),
                () -> assertThat(orderTable.isEmpty()).isTrue(),
                () -> assertThat(orderTable2.getTableGroup()).isNull(),
                () -> assertThat(orderTable2.isEmpty()).isTrue()
        );
    }
}
