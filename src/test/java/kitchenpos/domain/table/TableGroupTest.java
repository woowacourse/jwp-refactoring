package kitchenpos.domain.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.exception.badrequest.AlreadyGroupedException;
import kitchenpos.exception.badrequest.InvalidOrderTableSizeException;
import kitchenpos.exception.badrequest.OrderTableNotEmptyException;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    void 단체_지정된_테이블의_크기가_2_미만일_수_없다() {
        OrderTable orderTable = new OrderTable(0, true);

        assertThatThrownBy(() -> new TableGroup(List.of(orderTable)))
                .isInstanceOf(InvalidOrderTableSizeException.class);
    }

    @Test
    void 빈_테이블이_아닌_테이블을_단체_지정할_수_없다() {
        OrderTable orderTable1 = new OrderTable(0, true);
        OrderTable orderTable2 = new OrderTable(0, false);

        assertThatThrownBy(() -> new TableGroup(List.of(orderTable1, orderTable2)))
                .isInstanceOf(OrderTableNotEmptyException.class);
    }

    @Test
    void 이미_단체_지정된_테이블을_단체_지정할_수_없다() {
        OrderTable orderTable1 = new OrderTable(0, true);
        OrderTable orderTable2 = new OrderTable(0, true);
        new TableGroup(List.of(orderTable1, orderTable2));

        assertThatThrownBy(() -> new TableGroup(List.of(orderTable1, orderTable2)))
                .isInstanceOf(AlreadyGroupedException.class);
    }

    @Test
    void 단체_지정된_테이블들의_단체_지정을_해제할_수_있다() {
        OrderTable orderTable1 = new OrderTable(0, true);
        OrderTable orderTable2 = new OrderTable(0, true);
        TableGroup tableGroup = new TableGroup(List.of(orderTable1, orderTable2));

        tableGroup.ungroup();

        assertAll(
                () -> assertThat(orderTable1.getTableGroup()).isNull(),
                () -> assertThat(orderTable2.getTableGroup()).isNull()
        );
    }
}
