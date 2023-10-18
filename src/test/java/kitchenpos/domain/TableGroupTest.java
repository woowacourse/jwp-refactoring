package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableGroupTest {

    @Nested
    class 테이블을_그룹화_할떄 {

        @Test
        void 성공() {
            // given
            List<OrderTable> orderTables = List.of(
                new OrderTable(3, true),
                new OrderTable(2, true)
            );
            TableGroup tableGroup = TableGroup.createEmpty();

            // when && then
            assertThatNoException().isThrownBy(() -> tableGroup.group(orderTables));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1})
        void 속한_테이블의_갯수가_2개_미만이면_예외(int size) {
            // given
            List<OrderTable> orderTables = new ArrayList<>();
            for (long i = 1; i <= size; i++) {
                orderTables.add(new OrderTable(i, null, 2, false));
            }
            TableGroup tableGroup = TableGroup.createEmpty();

            // when && then
            assertThatThrownBy(() -> tableGroup.group(orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("테이블 그룹은 최소 2개 이상의 테이블이 필요합니다.");
        }

        @Test
        void 각_테이블은_모두_비어있지_않으면_예외() {
            // given
            List<OrderTable> orderTables = List.of(
                new OrderTable(1L, null, 3, true),
                new OrderTable(2L, null, 2, false)
            );
            TableGroup tableGroup = TableGroup.createEmpty();

            // when && then
            assertThatThrownBy(() -> tableGroup.group(orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비어있는 테이블만 주문그룹이 될 수 있습니다.");
        }

        @Test
        void 이미_그룹에_속한_테이블이_존재하면_예외() {
            // given
            List<OrderTable> orderTables = List.of(
                new OrderTable(1L, null, 3, true),
                new OrderTable(2L, 1L, 2, false)
            );
            TableGroup tableGroup = TableGroup.createEmpty();

            // when && then
            assertThatThrownBy(() -> tableGroup.group(orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비어있는 테이블만 주문그룹이 될 수 있습니다.");
        }
    }

    @Test
    void 테이블_그룹을_해제한다() {
        // given
        TableGroup tableGroup = new TableGroup(1L);
        tableGroup.group(List.of(new OrderTable(3, true), new OrderTable(2, true)));

        // when
        tableGroup.ungroup();

        // then
        assertThat(tableGroup.getOrderTables()).usingRecursiveComparison()
            .isEqualTo(List.of(
                new OrderTable(3, true),
                new OrderTable(2, true)));
    }
}

