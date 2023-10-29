package kitchenpos.domain.tablegroup;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableGroupTest {

    @Test
    void 단체_지정은_주문_테이블이_2개보다_작으면_예외가_발생한다() {
        // given
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        // expect
        assertThatThrownBy(() -> tableGroup.group(List.of(1L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블은 2개 이상이여야 합니다");
    }
}
