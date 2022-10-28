package kitchenpos.domain;

import static kitchenpos.support.TestFixtureFactory.단체_지정을_생성한다;
import static kitchenpos.support.TestFixtureFactory.주문_테이블을_생성한다;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
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
}
