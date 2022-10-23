package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;

@DisplayName("TableGroupService의")
class TableGroupServiceTest extends ServiceTest {

    @Nested
    @DisplayName("create 메서드는")
    class Create {

        @Test
        @DisplayName("단체 지정을 등록할 수 있다.")
        void create_validOrderTableIds_success() {
            // given
            final OrderTable orderTable1 = saveOrderTable(1, true);
            final OrderTable orderTable2 = saveOrderTable(2, true);

            final TableGroup expected = new TableGroup();
            expected.setOrderTables(List.of(orderTable1, orderTable2));

            // when
            final TableGroup actual = tableGroupService.create(expected);

            // then
            assertThat(actual.getOrderTables()).extracting(OrderTable::getTableGroupId, OrderTable::isEmpty,
                            OrderTable::getId)
                    .containsExactly(
                            tuple(actual.getId(), false, orderTable1.getId()),
                            tuple(actual.getId(), false, orderTable2.getId())
                    );
        }

        @Test
        @DisplayName("주문 테이블은 2개 미만일 수 없다.")
        void create_orderTableLessThenTwo_success() {
            // given
            final OrderTable orderTable = saveOrderTable(1, true);

            final TableGroup expected = new TableGroup();
            expected.setOrderTables(List.of(orderTable));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("모든 주문 테이블이 존재해야 한다.")
        void create_notExistOrderTable_success() {
            // given
            final OrderTable orderTable1 = saveOrderTable(1, true);
            final OrderTable orderTable2 = saveOrderTable(2, true);
            orderTable2.setId(999L);

            final TableGroup expected = new TableGroup();
            expected.setOrderTables(List.of(orderTable1, orderTable2));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("모든 주문 테이블은 빈 테이블이어야 한다.")
        void create_notEmptyOrderTable_success() {
            // given
            final OrderTable orderTable1 = saveOrderTable(1, true);
            final OrderTable orderTable2 = saveOrderTable(2, false);

            final TableGroup expected = new TableGroup();
            expected.setOrderTables(List.of(orderTable1, orderTable2));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("이미 단체 지정된 주문 테이블이 하나라도 포함되면 단체 지정을 할 수 없다.")
        void create_alreadyInTableGroup_exception() {
            // given
            final OrderTable orderTable1 = saveOrderTable(1, true);
            final OrderTable orderTable2 = saveOrderTable(2, false);
            final OrderTable orderTable3 = saveOrderTable(3, false);

            saveTableGroup(orderTable2, orderTable3);

            final TableGroup expected = new TableGroup();
            expected.setOrderTables(List.of(orderTable1, orderTable2));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("ungroup 메서드는")
    class Ungroup {

        @Test
        @DisplayName("단체 지정을 해제할 수 있다.")
        void ungroup_validTableGroup_success() {
            // given
            final OrderTable orderTable1 = saveOrderTable(1, true);
            final OrderTable orderTable2 = saveOrderTable(2, false);

            final Long tableGroupId = saveTableGroup(orderTable1, orderTable2).getId();

            // when & then
            assertThatCode(() -> tableGroupService.ungroup(tableGroupId))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("모든 주문 테이블에 주문이 있다면 주문 상태는 계산 완료여야 한다.")
        void ungroup_orderStatusIsNotCompletion_exception() {
            // given
            final OrderTable orderTable1 = saveOrderTable(1, true);
            final OrderTable orderTable2 = saveOrderTable(2, false);

            final Long tableGroupId = saveTableGroup(orderTable1, orderTable2).getId();

            final Product product = saveProduct("감자튀김");
            final MenuGroup menuGroup = saveMenuGroup("감자");
            final Menu menu = saveMenu("감자세트", BigDecimal.ONE, menuGroup, Pair.of(product, 1L));
            saveOrder(orderTable1, "COOKING", Pair.of(menu, 1L));
            saveOrder(orderTable2, "COMPLETION", Pair.of(menu, 2L));

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
