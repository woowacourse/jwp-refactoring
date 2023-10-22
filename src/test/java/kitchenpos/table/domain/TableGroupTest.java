package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class TableGroupTest {

    @DisplayName("테이블 그룹 테스트")
    @Nested
    class TableGroupCreateTest {

        @DisplayName("테이블 그룹을 생성한다.")
        @Test
        void tableGroupCreateTest() {
            //given
            final List<OrderTable> orderTables = List.of(new OrderTable(null, 3, true), new OrderTable(null, 3, true));

            //when
            final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);

            //then
            assertSoftly(softly -> {
                softly.assertThat(tableGroup.getId()).isNull();
                softly.assertThat(tableGroup.getOrderTables().size()).isEqualTo(2);
            });
        }

        @DisplayName("테이블이 2개 미만이면 실패한다.")
        @Test
        void tableGroupCreateFailWhenOrderTableSizeLessThenTwo() {
            //given
            final List<OrderTable> orderTables = List.of(new OrderTable(null, 3, true));

            // when & then
            assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), orderTables))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("그룹화 불가능한 테이블이면 실패한다.")
        @Test
        void tableGroupCreateFailWhenOrderTableIsNotGroupable() {
            //given
            final List<OrderTable> orderTables = List.of(new OrderTable(null, 3, false), new OrderTable(null, 3, false));

            // when & then
            assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), orderTables))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("그룹화 해제 테스트")
    @Nested
    class UnGroupTest {

        @DisplayName("그룹화를 해제한다.")
        @Test
        void unGroupTest() {
            //given
            final List<OrderTable> orderTables = List.of(new OrderTable(null, 3, true), new OrderTable(null, 3, true));
            final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);

            //when
            tableGroup.unGroup();

            //then
            assertSoftly(softly -> {
                softly.assertThat(tableGroup.getOrderTables().get(0).getTableGroup()).isNull();
                softly.assertThat(tableGroup.getOrderTables().get(1).getTableGroup()).isNull();
            });
        }
    }
}
