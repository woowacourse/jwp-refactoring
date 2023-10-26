package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.application.table.TableService;
import kitchenpos.application.tablegroup.TableGroupService;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.TableGroupRequest;

class TableGroupServiceTest extends BaseServiceTest{

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    private TableGroupRequest tableGroupRequest;

    @BeforeEach
    void setUp() {
        final OrderTableRequest request1 = new OrderTableRequest(4, true);
        final OrderTableRequest request2 = new OrderTableRequest(4, true);

        orderTable1 = tableService.create(request1);
        orderTable2 = tableService.create(request2);

        tableGroupRequest = new TableGroupRequest(List.of(orderTable1.getId(), orderTable2.getId()));
    }


    @Nested
    @DisplayName("테이블 생성 테스트")
    class CreateTableGroupTest {

        @Test
        @DisplayName("테이블 그룹을 생성한다.")
        void create() {
            //given & when
            final TableGroup savedTableGroup = tableGroupService.create(tableGroupRequest);

            //then
            final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
            final List<OrderTable> tables = orderTableRepository.findAllById(orderTableIds);
            final boolean match = tables.stream()
                    .map(OrderTable::getTableGroupId)
                    .anyMatch(groupId -> !groupId.equals(savedTableGroup.getId()));

            assertSoftly(softAssertions -> {
                assertThat(savedTableGroup.getId()).isNotNull();
                assertThat(match).isFalse();
            });
        }

        @Test
        @DisplayName("OrderTables 가 빈 리스트인 경우 예외가 발생한다.")
        void createWithEmptyCreateTables() {
            //given
            final TableGroupRequest tableGroupWithEmptyTables = new TableGroupRequest(List.of());

            //when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupWithEmptyTables))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("orderTables 갯수가 2 이하입니다.");
        }

        @Test
        @DisplayName("OrderTables의 size 1인경우 예외가 발생한다.")
        void createWithSingleOrderTable() {
            //given
            final TableGroupRequest tableGroupRequest1 = new TableGroupRequest(List.of(orderTable1.getId()));

            //when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("orderTables 갯수가 2 이하입니다.");
        }

        @Test
        @DisplayName("저장 되지 않은 OrderTable을 포함한 경우 예외가 발생한다.")
        void createdWithUnSavedOrderTable() {
            //given
            final OrderTable unSavedOrderTable = new OrderTable(-1L, null, 10, true);
            final TableGroupRequest tableGroupRequest1 = new TableGroupRequest(
                    List.of(orderTable1.getId(), unSavedOrderTable.getId()));

            //when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("유효하지 않은 OrderTable을 포함하고 있습니다.");
        }

        @Test
        @DisplayName("비어있지 않은 OrderTable을 포함한 경우 예외 발생")
        void createWithFullOrderTable() {
            //given
            final OrderTable unEmptyTable = tableService.create(new OrderTableRequest(4, false));
            final TableGroupRequest tableGroupRequest1 = new TableGroupRequest(
                    List.of(orderTable1.getId(), unEmptyTable.getId()));

            //when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("비어있지 않거나, 이미 그룹화된 테이블을 포함하고 있습니다.");
        }

        @Test
        @DisplayName("이미 그룹화 되어있는 테이블을 포함하고 있으면 예외가 발생한다.")
        void createWithAlreadyGroupedTable() {
            //given
            final TableGroupRequest tableGroupRequest1 = new TableGroupRequest(
                    List.of(orderTable1.getId(), orderTable2.getId()));
            tableGroupService.create(tableGroupRequest1);

            final OrderTable orderTable = tableService.create(new OrderTableRequest(4, true));

            final TableGroupRequest tableGroupRequest2 = new TableGroupRequest(
                    List.of(orderTable1.getId(), orderTable.getId()));

            //when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest2))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("비어있지 않거나, 이미 그룹화된 테이블을 포함하고 있습니다.");
        }
    }

    @Nested
    @DisplayName("테이블 그룹해제 테스트")
    class UnGroupTest {

        @Test
        @DisplayName("테이블의 그룹을 해제한다")
        void unGroupTables() {
            //given
            final TableGroup savedTableGroup = tableGroupService.create(tableGroupRequest);
            final List<Long> tableIds = orderTableRepository.findAllByGroupId(savedTableGroup.getId())
                    .stream()
                    .map(OrderTable::getId)
                    .collect(Collectors.toList());

            //when
            tableGroupService.ungroup(savedTableGroup.getId());


            //then
            final OrderTable orderTable1 = orderTableRepository.findById(tableIds.get(0)).get();
            final OrderTable orderTable2 = orderTableRepository.findById(tableIds.get(1)).get();

            assertSoftly(softAssertions -> {
                assertThat(orderTable1.isEmpty()).isFalse();
                assertThat(orderTable2.isEmpty()).isFalse();
                assertThat(orderTable1.getTableGroupId()).isNull();
                assertThat(orderTable2.getTableGroupId()).isNull();
            });
        }

        @DisplayName("테이블의 상태가 MEAL 또는 COOKING 이면 예외가 발생한다.")
        @ParameterizedTest(name = "status = {0}")
        @ValueSource(strings = {"MEAL", "COOKING"})
        void unGroupWithInvalidStatusTable(final String status) {
            //given
            final TableGroup savedTableGroup = tableGroupService.create(tableGroupRequest);

            final List<Long> tableIds = orderTableRepository.findAllByGroupId(savedTableGroup.getId())
                    .stream()
                    .map(OrderTable::getId)
                    .collect(Collectors.toList());
            final OrderTable orderTable = orderTableRepository.findById(tableIds.get(0)).get();
            final Order order = new Order(orderTable.getId(), null);
            orderRepository.save(order);

            //when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 주문이 진행 중이에요");
        }
    }
}
