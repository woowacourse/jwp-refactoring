package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest extends IntegrationTest {

    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        tableGroup = new TableGroup();
    }

    @Test
    void 주문_테이블들이_null이면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블들이_없으면_예외가_발생한다() {
        // given
        tableGroup.setOrderTables(List.of());

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블들이_하나면_예외가_발생한다() {
        // given
        OrderTable orderTable = new OrderTable();
        tableGroup.setOrderTables(List.of(orderTable));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_존재하지_않으면_예외가_발생한다() {
        // given
        OrderTable orderTable1 = new OrderTable();
        OrderTable orderTable2 = new OrderTable();
        tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Nested
    class 주문_테이블들이_있는경우 {

        private OrderTable orderTable1;
        private OrderTable orderTable2;

        @BeforeEach
        void setUp() {
            orderTable1 = new OrderTable();
            orderTable2 = new OrderTable();
        }

        @Test
        void 주문_테이블이_비어있지_않으면_예외가_발생한다() {
            // given
            orderTable1.setEmpty(false);
            OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
            OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);
            tableGroup.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 단체를_지정한다() {
            // given
            orderTable1.setEmpty(true);
            orderTable2.setEmpty(true);
            OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
            OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);
            tableGroup.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));

            // when
            TableGroup result = tableGroupService.create(tableGroup);

            // then
            assertAll(
                    () -> assertThat(result.getId()).isPositive(),
                    () -> assertThat(result.getOrderTables()).hasSize(2)
            );
        }

        @Nested
        class 단체가_지정되어_있는경우 {

            @Test
            void 주문_테이블에_이미_지정된_단체가_있으면_예외가_발생한다() {
                // given
                TableGroup 지정된_그룹 = 빈_테이블들을_그룹으로_지정한다();
                orderTable1.setEmpty(true);
                orderTable1.setTableGroupId(지정된_그룹.getId());
                OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
                orderTable2.setEmpty(true);
                OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);
                tableGroup.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));

                // when & then
                assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void 조리중이거나_식사중인_테이블의_그룹을_해제하면_예외가_발생한다() {
                // given
                orderTable1.setEmpty(true);
                OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
                주문(savedOrderTable1, OrderStatus.COOKING, 맛있는_메뉴());
                orderTable2.setEmpty(true);
                OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);
                tableGroup.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));
                TableGroup savedTableGroup = tableGroupService.create(tableGroup);

                // when & then
                assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }
}
