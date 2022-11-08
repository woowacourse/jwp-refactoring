package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    void construct_테이블그룹의_사이즈가_비었으면_예외를_반환한다() {
        assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void construct_테이블그룹의_사이즈가_2보다_작으면_예외를_반환한다() {
        assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(),
                List.of(new OrderTable(2, true))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void construct_각각의_오더_테이블이_비어있지_않으면_예외를_반환한다() {
        assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(),
                List.of(new OrderTable(2, false),
                        new OrderTable(2, false))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void construct_오더_테이블중_하나라도_비어있지_않으면_예외를_반환한다() {
        assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(),
                List.of(new OrderTable(2, false),
                        new OrderTable(2, true))))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
