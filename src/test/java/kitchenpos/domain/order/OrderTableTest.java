package kitchenpos.domain.order;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import org.junit.jupiter.api.Test;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @Test
    void 단체_지정_아이디가_null이_아니면_예외를_던진다() {
        assertThatThrownBy(() -> new OrderTable(new TableGroup(now()), 5, false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 저장된_주문_테이블이_비어있다면_실패한다() {
        // when, then
        assertThatThrownBy(() -> new OrderTable(3, false))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
