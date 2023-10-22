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
    void Id가_정의되지_않은_TableGroup_에_상품을_추가하면_예외() {
        // given
        TableGroup tableGroup = new TableGroup(null, LocalDateTime.now());

        // when && then
        assertThatThrownBy(() -> tableGroup.addOrderTable(Collections.emptyList()))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("TableGroup 의 식별자가 정의되지 않았습니다.");
    }

    @Test
    void 주문_테이블이_2보다_작으면_예외() {
        // given
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());

        // when && then
        assertThatThrownBy(() -> tableGroup.addOrderTable(Collections.emptyList()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("주문 테이블은 최소 2개 이상입니다.");
    }

    @Test
    void 주문_테이블이_차있다면_에외() {
        // given
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());

        List<OrderTable> orderTables = List.of(
            new OrderTable(null, null, 0, true),
            new OrderTable(null, null, 0, false)
        );

        // when && then
        assertThatThrownBy(() -> {
            tableGroup.addOrderTable(
                orderTables
            );
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("그룹화하기 위해서는 모든 테이블은 빈 테이블이여야 합니다.");
    }

    @Test
    void 이미_그룹화된_테이블이_있으면_예외() {
        // given
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());
        TableGroup oterTableGroup = new TableGroup(2L, LocalDateTime.now());

        List<OrderTable> orderTables = List.of(
            new OrderTable(null, null, 0, true),
            new OrderTable(null, oterTableGroup, 0, true)
        );

        // when && then
        assertThatThrownBy(() -> {
            tableGroup.addOrderTable(
                orderTables
            );
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("주문 테이블이 이미 그룹화 되어 있습니다.");
    }
}
