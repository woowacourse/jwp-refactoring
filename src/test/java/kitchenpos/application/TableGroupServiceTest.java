package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.order.domain.Order;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.dto.TableGroupCreateRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    List<OrderTable> orderTables;

    @BeforeEach
    void beforeEach() {
        OrderTable orderTable1 = new OrderTable(null, 3, true);
        orderTable1 = testFixtureBuilder.buildOrderTable(orderTable1);

        OrderTable orderTable2 = new OrderTable(null, 5, true);
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
            final TableGroupCreateRequest request = new TableGroupCreateRequest(orderTables.stream().map(OrderTable::getId).collect(Collectors.toList()));

            //when
            final Long id = tableGroupService.create(request);

            //then
            assertSoftly(softly -> {
                softly.assertThat(id).isNotNull();
            });
        }

        @DisplayName("주문 테이블들이 비어있으면 실패한다.")
        @Test
        void tableGroupCreateFailWhenOrderTablesIsEmpty() {
            //given
            final TableGroupCreateRequest request = new TableGroupCreateRequest(Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블들의 크기가 2개 미만이면 실패한다.")
        @Test
        void tableGroupCreateFailWhenOrderTablesSizeLessThenTwo() {
            //given
            final TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(orderTables.get(0).getId()));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 존재하지 않으면 실패한다.")
        @Test
        void tableGroupCreateFailWhenNotExistOrderTable() {
            //given
            final TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(-1L));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 비어있지 않으면 실패한다.")
        @Test
        void tableGroupCreateFailWhenOrderTableIsNotEmpty() {
            //given
            OrderTable notEmptyOrderTable = new OrderTable(null, 3, false);
            notEmptyOrderTable = testFixtureBuilder.buildOrderTable(notEmptyOrderTable);
            orderTables.add(notEmptyOrderTable);

            final TableGroupCreateRequest request = new TableGroupCreateRequest(orderTables.stream().map(OrderTable::getId).collect(Collectors.toList()));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블의 단체 지정 id가 null이 아니면 실패한다.")
        @Test
        void tableGroupCreateFailWhenOrderTableIsNotNullTableGroupId() {
            //given
            TableGroup beforeTableGroup = new TableGroup(LocalDateTime.now(), orderTables);
            beforeTableGroup = testFixtureBuilder.buildTableGroup(beforeTableGroup);

            OrderTable tableGroupIdNotNullOrderTable = new OrderTable(beforeTableGroup.getId(), 3, true);
            tableGroupIdNotNullOrderTable = testFixtureBuilder.buildOrderTable(tableGroupIdNotNullOrderTable);

            orderTables.add(tableGroupIdNotNullOrderTable);
            final TableGroupCreateRequest request = new TableGroupCreateRequest(orderTables.stream().map(OrderTable::getId).collect(Collectors.toList()));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
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
            TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
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
            TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
            tableGroup = testFixtureBuilder.buildTableGroup(tableGroup);

            OrderTable orderTable = new OrderTable(tableGroup.getId(), 3, false);
            orderTable = testFixtureBuilder.buildOrderTable(orderTable);

            final Order notCompletionOrder = new Order(orderTable.getId(), orderStatus, LocalDateTime.now(), Collections.emptyList());
            testFixtureBuilder.buildOrder(notCompletionOrder);

            // when & then
            final Long tableGroupId = tableGroup.getId();
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
