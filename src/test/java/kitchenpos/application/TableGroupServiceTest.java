package kitchenpos.application;

import kitchenpos.application.config.ServiceTestConfig;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.request.TableGroupCreateRequest;
import kitchenpos.ui.dto.response.OrderTableInTableGroupResponse;
import kitchenpos.ui.dto.response.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class TableGroupServiceTest extends ServiceTestConfig {

    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderRepository, orderTableRepository, tableGroupRepository);
    }

    @DisplayName("주문 테이블 그룹 생성")
    @Nested
    class Create {
        @DisplayName("성공한다.")
        @Test
        void success() {
            // given
            final OrderTable orderTable1 = saveEmptyOrderTable();
            final OrderTable orderTable2 = saveEmptyOrderTable();
            final List<Long> orderTableIdsInput = List.of(orderTable1.getId(), orderTable2.getId());
            final TableGroupCreateRequest request = new TableGroupCreateRequest(orderTableIdsInput);

            // when
            final TableGroupResponse actual = tableGroupService.create(request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getOrderTables().size()).isEqualTo(orderTableIdsInput.size());
                softly.assertThat(actual.getOrderTables().stream()
                                .map(OrderTableInTableGroupResponse::getId))
                        .isEqualTo(orderTableIdsInput);
                softly.assertThat(actual.getOrderTables().stream()
                                .map(OrderTableInTableGroupResponse::isEmpty))
                        .containsOnly(false);
            });
        }

        @DisplayName("OrderTables 가 비어있으면 실패한다.")
        @Test
        void fail_if_orderTables_are_empty() {
            // given
            final TableGroupCreateRequest request = new TableGroupCreateRequest(new ArrayList<>());

            // then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("OrderTables 에 1개의 원소만 들어있다면 실패한다.")
        @Test
        void fail_if_orderTables_size_is_one() {
            // given
            final OrderTable orderTable = saveEmptyOrderTable();
            final TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(orderTable.getId()));

            // then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("잘못된 OrderTableId 가 있으면 실패한다.")
        @Test
        void fail_if_orderTables_contains_invalid_orderTableId() {
            // given
            final OrderTable orderTable = saveEmptyOrderTable();
            final Long invalidOrderTableId = -1L;
            final TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(orderTable.getId(), invalidOrderTableId));

            // then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("착석되어있는 OrderTable 이 존재하면 실패한다.")
        @Test
        void fail_orderTables_contains_not_empty_orderTable() {
            // given
            final OrderTable orderTable1 = saveEmptyOrderTable();
            final OrderTable orderTable2 = saveOccupiedOrderTable();
            final List<Long> orderTableIdsInput = List.of(orderTable1.getId(), orderTable2.getId());
            final TableGroupCreateRequest request = new TableGroupCreateRequest(orderTableIdsInput);

            // then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("이미 그룹 지정된 OrderTable 이 존재하면 실패한다.")
        @Test
        void fail_orderTables_contains_already_grouping() {
            // given
            final OrderTable orderTable1 = saveEmptyOrderTable();
            orderTable1.changeToOccupied();
            final OrderTable orderTable2 = saveEmptyOrderTable();
            orderTable2.changeToOccupied();
            final TableGroup savedTableGroup = saveTableGroup(List.of(orderTable1, orderTable2));

            final TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(orderTable1.getId(), orderTable2.getId()));

            // then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 테이블 그룹 삭제")
    @Nested
    class UnGroup {
//        @DisplayName("주문 테이블 그룹의 주문들이 모두 계산 상태라면 성공한다.")
//        @Test
//        void success_if_all_orders_orderStatus_are_COMPLETION() {
//            // given
//            final OrderTable orderTable1 = saveEmptyOrderTable();
//            orderTable1.changeToOccupied();
//            final OrderTable orderTable2 = saveEmptyOrderTable();
//            orderTable2.changeToOccupied();
//            final TableGroup tableGroup = saveTableGroup(List.of(orderTable1, orderTable2));
//
//            // TODO: order 상태 변경
//
//            final Long tableGroupId = tableGroup.getId();
//
//            // 주문 테이블 그루핑
//            final OrderTable orderTable1 = new OrderTable();
//            orderTable1.setEmpty(true);
//            orderTable1.setTableGroupId(tableGroup.getId());
//            final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
//            final OrderTable orderTable2 = new OrderTable();
//            orderTable2.setEmpty(true);
//            orderTable2.setTableGroupId(tableGroup.getId());
//            final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);
//
//            // 주문 테이블에 주문 넣기
//            final Order order1 = new Order();
//            order1.setOrderTableId(savedOrderTable1.getId());
//            order1.setOrderStatus(OrderStatus.COMPLETION.name());
//            order1.setOrderedTime(LocalDateTime.now());
//            orderDao.save(order1);
//            final Order order2 = new Order();
//            order2.setOrderTableId(savedOrderTable2.getId());
//            order2.setOrderStatus(OrderStatus.COMPLETION.name());
//            order2.setOrderedTime(LocalDateTime.now());
//            orderDao.save(order2);
//
//            // when
//            tableGroupService.ungroup(tableGroup.getId());
//
//            // then
//            final List<OrderTable> updatedOrderTables = orderTableDao.findAllByIdIn(List.of(savedOrderTable1.getId(), savedOrderTable2.getId()));
//            final List<Long> filteredOrderTablesId = updatedOrderTables.stream()
//                    .filter(orderTable -> orderTable.getTableGroup() == null)
//                    .filter(orderTable -> !orderTable.isEmpty())
//                    .map(OrderTable::getId)
//                    .collect(Collectors.toUnmodifiableList());
//
//            assertThat(filteredOrderTablesId).containsExactly(savedOrderTable1.getId(), savedOrderTable2.getId());
//        }
//
//        @DisplayName("주문 테이블 그룹의 주문들 일부가 완료 상태가 아니라면 실패한다.")
//        @Test
//        void fail_if_some_orders_orderStatus_are_not_COMPLETION() {
//            // given
//            final TableGroup tableGroup = saveTableGroup();
//
//            final OrderTable orderTable1 = new OrderTable();
//            orderTable1.setEmpty(true);
//            orderTable1.setTableGroupId(tableGroup.getId());
//            final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
//            final OrderTable orderTable2 = new OrderTable();
//            orderTable2.setEmpty(true);
//            orderTable2.setTableGroupId(tableGroup.getId());
//            final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);
//
//            final Order order1 = new Order();
//            order1.setOrderTableId(savedOrderTable1.getId());
//            order1.setOrderStatus(OrderStatus.COOKING.name());
//            order1.setOrderedTime(LocalDateTime.now());
//            orderDao.save(order1);
//            final Order order2 = new Order();
//            order2.setOrderTableId(savedOrderTable2.getId());
//            order2.setOrderStatus(OrderStatus.COMPLETION.name());
//            order2.setOrderedTime(LocalDateTime.now());
//            orderDao.save(order2);
//
//            // then
//            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
//                    .isInstanceOf(IllegalArgumentException.class);
//        }
    }
}
