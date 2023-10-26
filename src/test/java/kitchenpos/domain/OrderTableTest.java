package kitchenpos.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.table.exception.IllegalOrderTableGuestNumberException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

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
        final TableGroup tableGroup = new TableGroup();

        //when
        orderTable.changeTableGroup(tableGroup.getId());

        //then
        assertAll(
                () -> assertThat(orderTable.getTableGroupId()).isEqualTo(tableGroup.getId()),
                () -> assertThat(orderTable2.getTableGroupId()).isEqualTo(tableGroup.getId())
        );
    }

    @Test
    void ungroup() {
        //given
        final OrderTable orderTable = new OrderTable(null, 3, false);
        final OrderTable orderTable2 = new OrderTable(null, 3, false);
        final TableGroup tableGroup = new TableGroup();
        orderTable.changeTableGroup(tableGroup.getId());

        //when
        orderTable.ungroup();
        orderTable2.ungroup();

        //then
        assertAll(
                () -> assertThat(orderTable.getTableGroupId()).isNull(),
                () -> assertThat(orderTable.isEmpty()).isTrue(),
                () -> assertThat(orderTable2.getTableGroupId()).isNull(),
                () -> assertThat(orderTable2.isEmpty()).isTrue()
        );
    }
}
