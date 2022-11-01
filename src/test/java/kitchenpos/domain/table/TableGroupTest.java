package kitchenpos.domain.table;

import static kitchenpos.support.TestFixtureFactory.단체_지정을_생성한다;
import static kitchenpos.support.TestFixtureFactory.주문_테이블을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    void 단체_지정된_테이블의_크기가_2_미만일_수_없다() {
        OrderTable orderTable = 주문_테이블을_생성한다(null, 0, true);

        assertThatThrownBy(() -> new TableGroup(null, LocalDateTime.now(), List.of(orderTable)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 빈_테이블이_아닌_테이블을_단체_지정할_수_없다() {
        OrderTable orderTable1 = 주문_테이블을_생성한다(null, 0, true);
        OrderTable orderTable2 = 주문_테이블을_생성한다(null, 0, false);

        assertThatThrownBy(() -> new TableGroup(null, LocalDateTime.now(), List.of(orderTable1, orderTable2)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이미_단체_지정된_테이블을_단체_지정할_수_없다() {
        OrderTable orderTable1 = 주문_테이블을_생성한다(null, 0, true);
        OrderTable orderTable2 = 주문_테이블을_생성한다(null, 0, true);
        단체_지정을_생성한다(LocalDateTime.now(), List.of(orderTable1, orderTable2));

        assertThatThrownBy(() -> new TableGroup(null, LocalDateTime.now(), List.of(orderTable1, orderTable2)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정된_테이블들의_단체_지정을_해제할_수_있다() {
        OrderTable orderTable1 = 주문_테이블을_생성한다(null, 0, true);
        OrderTable orderTable2 = 주문_테이블을_생성한다(null, 0, true);
        TableGroup tableGroup = 단체_지정을_생성한다(LocalDateTime.now(), List.of(orderTable1, orderTable2));

        tableGroup.ungroup();

        assertAll(
                () -> assertThat(orderTable1.getTableGroup()).isNull(),
                () -> assertThat(orderTable2.getTableGroup()).isNull()
        );
    }
}
