package kitchenpos.domain.order;

import org.junit.jupiter.api.Test;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @Test
    void 단체_지정_아이디가_null이_아니면_예외를_던진다() {
        assertThatThrownBy(() -> new OrderTable(new TableGroup(now(), new OrderTables()), 5, false))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
