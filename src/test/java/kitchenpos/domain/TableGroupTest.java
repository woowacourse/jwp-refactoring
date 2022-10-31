package kitchenpos.domain;

import static kitchenpos.support.OrderTableFixture.ORDER_TABLE_EMPTY_1;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    void 테이블_개수가_2개_미만이면_예외를_반환한다() {
        // given, when, then
        assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), List.of(ORDER_TABLE_EMPTY_1.생성())))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
