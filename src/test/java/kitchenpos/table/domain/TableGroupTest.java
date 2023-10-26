package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.table.domain.model.OrderTable;
import kitchenpos.table.domain.model.TableGroup;
import kitchenpos.table.supports.OrderTableFixture;
import kitchenpos.table.supports.TableGroupFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableGroupTest {

    @Nested
    class 테이블_추가 {

        @Test
        void 테이블이_비어있으면_예외() {
            // given
            List<OrderTable> orderTables = List.of();
            TableGroup tableGroup = TableGroupFixture.fixture().build();

            // when & then
            assertThatThrownBy(() -> tableGroup.setUpOrderTable(orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블은 2개 이상이어야 합니다.");
        }

        @Test
        void 테이블이_2개보다_적으면_예외() {
            // given
            List<OrderTable> orderTables = List.of(
                OrderTableFixture.fixture().build()
            );
            TableGroup tableGroup = TableGroupFixture.fixture().build();

            // when & then
            assertThatThrownBy(() -> tableGroup.setUpOrderTable(orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블은 2개 이상이어야 합니다.");
        }

        @Test
        void 테이블_추가() {
            // given
            List<OrderTable> orderTables = List.of(
                OrderTableFixture.fixture().build(),
                OrderTableFixture.fixture().build()
            );
            TableGroup tableGroup = TableGroupFixture.fixture().build();

            // when
            tableGroup.setUpOrderTable(orderTables);

            // then
            assertThat(tableGroup.getOrderTables())
                .usingRecursiveComparison()
                .isEqualTo(orderTables);
        }
    }

    @Nested
    class 그룹_해제 {

        @Test
        void 모든_테이블이_그룹해제_가능하지_않으면_예외() {
            // given
            TableGroup tableGroup = TableGroupFixture.fixture().build();
            List<OrderTable> orderTables = List.of(
                OrderTableFixture.fixture().empty(true).tableGroup(tableGroup).build(),
                OrderTableFixture.fixture().empty(false).tableGroup(tableGroup).build()
            );
            tableGroup.setUpOrderTable(orderTables);

            // when & then
            assertThatThrownBy(tableGroup::ungroup)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹 해제할 수 없습니다.");
        }

        @Test
        void 그룹_해제() {
            // given
            TableGroup tableGroup = TableGroupFixture.fixture().build();
            List<OrderTable> orderTables = List.of(
                OrderTableFixture.fixture().empty(false).tableGroup(tableGroup).build(),
                OrderTableFixture.fixture().empty(false).tableGroup(tableGroup).build()
            );
            tableGroup.setUpOrderTable(orderTables);

            // when
            tableGroup.ungroup();

            // then
            assertThat(tableGroup.getOrderTables().stream().noneMatch(OrderTable::isEmpty))
                .isTrue();
        }
    }
}
