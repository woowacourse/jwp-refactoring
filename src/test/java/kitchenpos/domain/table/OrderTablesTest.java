package kitchenpos.domain.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderTablesTest {

    @Test
    void 생성자_호출시_테이블의_개수가_2개_미만인_경우_예외발생() {
        OrderTable emptyTable = new OrderTable(0, true);

        assertThatThrownBy(() -> new OrderTables(List.of(emptyTable)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("group 메서드는")
    @Nested
    class GroupTest {

        @Test
        void 빈_테이블들을_동일한_테이블그룹에_등록한다() {
            OrderTable emptyTable1 = new OrderTable(0, true);
            OrderTable emptyTable2 = new OrderTable(0, true);
            OrderTables orderTables = new OrderTables(List.of(emptyTable1, emptyTable2));
            long tableGroupId = 10L;

            orderTables.group(tableGroupId);
            List<OrderTable> actual = orderTables.getValue();
            assertAll(
                    () -> assertThat(actual.get(0).isEmpty()).isFalse(),
                    () -> assertThat(actual.get(0).getTableGroupId()).isEqualTo(tableGroupId),
                    () -> assertThat(actual.get(1).isEmpty()).isFalse(),
                    () -> assertThat(actual.get(1).getTableGroupId()).isEqualTo(tableGroupId)
            );
        }

        @Test
        void 비어있지_않은_테이블이_포함된_경우_예외발생() {
            OrderTables orderTables = new OrderTables(List.of(
                    new OrderTable(0, true),
                    new OrderTable(1, false)));

            assertThatThrownBy(() -> orderTables.group(1L))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 이미_다른_그룹에_포함된_테이블이_포함된_경우_예외발생() {
            OrderTables orderTables = new OrderTables(List.of(
                    new OrderTable(0, true),
                    new OrderTable(1L, 1L, 0, true)));

            assertThatThrownBy(() -> orderTables.group(1L))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
