package kitchenpos.application;

import kitchenpos.application.config.ServiceTestConfig;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class TableGroupServiceTest extends ServiceTestConfig {

    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
    }

    @DisplayName("주문 테이블 그룹 생성")
    @Nested
    class Create {
        @DisplayName("성공한다.")
        @Test
        void success() {
            // given
            final OrderTable orderTable1 = saveOrderTable();
            orderTable1.setEmpty(true);
            final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
            final OrderTable orderTable2 = saveOrderTable();
            orderTable2.setEmpty(true);
            final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);
            final TableGroup tableGroupInput = new TableGroup();
            tableGroupInput.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));

            // when
            final TableGroup actual = tableGroupService.create(tableGroupInput);

            // then
            // FIXME: equals&hashcode 적용
            assertSoftly(softly -> {
//               softly.assertThat(actual.getOrderTables()).containsExactly(orderTable1, orderTable2);
            });
        }

        @DisplayName("OrderTables 가 비어있으면 실패한다.")
        @Test
        void fail_if_orderTables_are_empty() {
            // given
            final TableGroup tableGroupInput = new TableGroup();
            tableGroupInput.setOrderTables(Collections.emptyList());

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupInput))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("OrderTables 에 1개의 원소만 들어있다면 실패한다.")
        @Test
        void fail_if_orderTables_size_is_one() {
            // given
            final OrderTable orderTable = saveOrderTable();
            orderTable.setEmpty(true);
            final OrderTable savedOrderTable = orderTableDao.save(orderTable);
            final TableGroup tableGroupInput = new TableGroup();
            tableGroupInput.setOrderTables(List.of(savedOrderTable));

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupInput))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("잘못된 OrderTableId 가 있으면 실패한다.")
        @Test
        void fail_if_orderTables_contains_invalid_orderTableId() {
            // given
            final OrderTable orderTable = saveOrderTable();
            orderTable.setEmpty(true);
            orderTable.setId(-1L);
            final OrderTable savedOrderTable = orderTableDao.save(orderTable);

            final TableGroup tableGroupInput = new TableGroup();
            tableGroupInput.setOrderTables(List.of(savedOrderTable));

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupInput))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 등록 가능한 상태의 OrderTable 이 존재하면 실패한다.")
        @Test
        void fail_orderTables_contains_not_empty_orderTable() {
            // given
            final OrderTable orderTable1 = saveOrderTable();
            final OrderTable orderTable2 = saveOrderTable();

            final TableGroup tableGroupInput = new TableGroup();
            tableGroupInput.setOrderTables(List.of(orderTable1, orderTable2));

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupInput))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("이미 그룹이 있는 OrderTable 이 존재하면 실패한다.")
        @Test
        void fail_orderTables_contains_already_grouping() {
            // given
            final OrderTable orderTable1 = saveOrderTable(saveTableGroup());
            orderTable1.setEmpty(true);
            final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
            final OrderTable orderTable2 = saveOrderTable(saveTableGroup());
            orderTable2.setEmpty(true);
            final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

            final TableGroup tableGroupInput = new TableGroup();
            tableGroupInput.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupInput))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
