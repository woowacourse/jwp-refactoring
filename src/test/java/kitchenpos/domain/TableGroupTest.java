package kitchenpos.domain;

import static kitchenpos.exception.ExceptionType.INVALID_TABLE_GROUP_EXCEPTION;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    void 없는_테이블에_그룹을_생성하면_예외를_반환한다() {
        Assertions.assertThatThrownBy(() -> new TableGroup(1L, LocalDateTime.now(), new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_TABLE_GROUP_EXCEPTION.getMessage());
    }

    @Test
    void 테이블_개수가_두개_미만일때_그룹을_생성하면_예외를_반환한다() {
        final OrderTable 테이블1 = new OrderTable(1L, null, 1, false);

        Assertions.assertThatThrownBy(() -> new TableGroup(1L, LocalDateTime.now(), Arrays.asList(테이블1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_TABLE_GROUP_EXCEPTION.getMessage());
    }
}
