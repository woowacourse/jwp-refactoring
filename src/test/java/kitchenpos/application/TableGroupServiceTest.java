package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    List<OrderTable> orderTables;

    @BeforeEach
    void beforeEach() {
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        orderTable1.setNumberOfGuests(3);
        orderTable1.setTableGroupId(null);
        orderTable1 = testFixtureBuilder.buildOrderTable(orderTable1);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        orderTable2.setNumberOfGuests(5);
        orderTable2.setTableGroupId(null);
        orderTable2 = testFixtureBuilder.buildOrderTable(orderTable2);

        orderTables = new ArrayList<>();
        orderTables.add(orderTable1);
        orderTables.add(orderTable2);
    }

    @DisplayName("단체 지정 생성 테스트")
    @Nested
    class TableGroupCreateTest {

        @DisplayName("단체 지정을 생성한다.")
        @Test
        void tableGroupCreate() {
            //given
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(orderTables);

            //when
            final TableGroup actual = tableGroupService.create(tableGroup);

            //then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isNotNull();
            });
        }

        @DisplayName("주문 테이블들이 비어있으면 실패한다.")
        @Test
        void tableGroupCreateFailWhenOrderTablesIsEmpty() {
            //given
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블들의 크기가 2개 미만이면 실패한다.")
        @Test
        void tableGroupCreateFailWhenOrderTablesSizeLessThenTwo() {
            //given
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(orderTables.get(0)));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 존재하지 않으면 실패한다.")
        @Test
        void tableGroupCreateFailWhenNotExistOrderTable() {
            //given
            final OrderTable notExistOrderTable = new OrderTable();
            notExistOrderTable.setId(-1L);
            notExistOrderTable.setEmpty(true);
            notExistOrderTable.setTableGroupId(null);
            notExistOrderTable.setNumberOfGuests(3);
            orderTables.add(notExistOrderTable);

            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(orderTables);

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 비어있지 않으면 실패한다.")
        @Test
        void tableGroupCreateFailWhenOrderTableIsNotEmpty() {
            //given
            OrderTable notEmptyOrderTable = new OrderTable();
            notEmptyOrderTable.setEmpty(false);
            notEmptyOrderTable.setTableGroupId(null);
            notEmptyOrderTable.setNumberOfGuests(3);
            notEmptyOrderTable = testFixtureBuilder.buildOrderTable(notEmptyOrderTable);
            orderTables.add(notEmptyOrderTable);

            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(orderTables);

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블의 단체 지정 id가 null이 아니면 실패한다.")
        @Test
        void tableGroupCreateFailWhenOrderTableIsNotNullTableGroupId() {
            //given
            TableGroup beforeTableGroup = new TableGroup();
            beforeTableGroup.setCreatedDate(LocalDateTime.now());
            beforeTableGroup.setOrderTables(orderTables);
            beforeTableGroup = testFixtureBuilder.buildTableGroup(beforeTableGroup);

            OrderTable tableGroupIdNotNullOrderTable = new OrderTable();
            tableGroupIdNotNullOrderTable.setEmpty(true);
            tableGroupIdNotNullOrderTable.setTableGroupId(beforeTableGroup.getId());
            tableGroupIdNotNullOrderTable.setNumberOfGuests(3);
            tableGroupIdNotNullOrderTable = testFixtureBuilder.buildOrderTable(tableGroupIdNotNullOrderTable);

            final TableGroup tableGroup = new TableGroup();
            orderTables.add(tableGroupIdNotNullOrderTable);
            tableGroup.setOrderTables(orderTables);
            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("단체 지정 해제 테스트")
    @Nested
    class TableGroupUpGroupTest {

        @DisplayName("단체 지정을 해제한다.")
        @Test
        void tableGroupUpGroup() {
            //given
            TableGroup tableGroup = new TableGroup();
            tableGroup.setCreatedDate(LocalDateTime.now());
            tableGroup.setOrderTables(orderTables);
            tableGroup = testFixtureBuilder.buildTableGroup(tableGroup);

            //when
            final Long tableGroupId = tableGroup.getId();
            assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroupId));
        }

        @DisplayName("단체 지정된 주문 테이블 중 조리나 식사 상태인 것이 있으면 실패한다.")
        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        void tableGroupUpGroupFailWhenOrderStatusInCookingOrMeal(final String orderStatus) {
            //given
            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(orderTables);
            tableGroup.setCreatedDate(LocalDateTime.now());
            tableGroup = testFixtureBuilder.buildTableGroup(tableGroup);

            OrderTable notCompletionOrdertable = new OrderTable();
            notCompletionOrdertable.setEmpty(false);
            notCompletionOrdertable.setTableGroupId(tableGroup.getId());
            notCompletionOrdertable.setNumberOfGuests(3);
            notCompletionOrdertable = testFixtureBuilder.buildOrderTable(notCompletionOrdertable);

            final Order notCompletionOrder = new Order();
            notCompletionOrder.setOrderStatus(orderStatus);
            notCompletionOrder.setOrderedTime(LocalDateTime.now());
            notCompletionOrder.setOrderTableId(notCompletionOrdertable.getId());
            testFixtureBuilder.buildOrder(notCompletionOrder);

            // when & then
            final Long tableGroupId = tableGroup.getId();
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
