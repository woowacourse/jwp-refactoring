package kitchenpos.domain.table;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    void 생성_시_주문테이블이_빈_경우_예외가_발생한다() {
        assertThatThrownBy(() -> new TableGroup(1L, LocalDateTime.now(), new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 최소 2개 이상이어야 단체 지정(grouping)이 가능합니다.");
    }

    @Test
    void 생성_시_주문테이블이_1개인_경우_예외가_발생한다() {
        assertThatThrownBy(() -> new TableGroup(1L, LocalDateTime.now(), Arrays.asList(new OrderTable())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 최소 2개 이상이어야 단체 지정(grouping)이 가능합니다.");
    }

    @Test
    void 생성_시_주문테이블이_비어있지않은_경우_예외가_발생한다() {
        assertThatThrownBy(() -> new TableGroup(1L, LocalDateTime.now(),
                Arrays.asList(new OrderTable(1, false), new OrderTable(0, true))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 비어있지 않거나 이미 단체지정되어있습니다.");
    }

    @Test
    void 생성_시_주문테이블이_단체지정된_경우_예외가_발생한다() {
        assertThatThrownBy(() -> new TableGroup(1L, LocalDateTime.now(),
                Arrays.asList(new OrderTable(1L, 2L, 0, true), new OrderTable(0, true))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 비어있지 않거나 이미 단체지정되어있습니다.");
    }
}
