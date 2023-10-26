package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableGroupTest {

    @Test
    void 주문_테이블이_2보다_작으면_예외() {
        // expect
        assertThatThrownBy(() -> new TableGroup(1L, LocalDateTime.now(), Collections.emptyList()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("주문 테이블은 최소 2개 이상입니다.");
    }

    @Test
    void 주문_테이블이_차있다면_에외() {
        // given
        List<OrderTable> orderTables = List.of(
            new OrderTable(null, null, 0, true),
            new OrderTable(null, null, 0, false)
        );

        // when && then
        assertThatThrownBy(() -> {
            new TableGroup(1L, LocalDateTime.now(), orderTables);
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("그룹화하기 위해서는 모든 테이블은 빈 테이블이여야 합니다.");
    }

    @Test
    void 이미_그룹화된_테이블이_있으면_예외() {
        // given
        TableGroup oterTableGroup = new TableGroup(2L, LocalDateTime.now(), List.of(
            new OrderTable(1L, null, 10, true),
            new OrderTable(2L, null, 7, true)
        ));

        List<OrderTable> orderTables = List.of(
            new OrderTable(null, null, 0, true),
            new OrderTable(null, oterTableGroup, 10, true)
        );

        // when && then
        assertThatThrownBy(() -> {
            new TableGroup(1L, LocalDateTime.now(), orderTables);
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("주문 테이블이 이미 그룹화 되어 있습니다.");
    }
}
