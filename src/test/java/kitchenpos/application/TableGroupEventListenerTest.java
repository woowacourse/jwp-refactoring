package kitchenpos.application;

import kitchenpos.application.config.ServiceTestConfig;
import kitchenpos.order.Order;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.application.validator.OrderStatusValidatorImpl;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.application.event.TableGroupEventListener;
import kitchenpos.ordertable.application.validator.OrderStatusValidator;
import kitchenpos.tablegroup.TableGroup;
import kitchenpos.tablegroup.application.event.dto.TableGroupCreateRequestEvent;
import kitchenpos.tablegroup.application.event.dto.TableGroupDeleteRequestEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TableGroupEventListenerTest extends ServiceTestConfig {

    private TableGroupEventListener tableGroupEventListener;

    @BeforeEach
    void setUp() {
        final OrderStatusValidator orderStatusValidator = new OrderStatusValidatorImpl(orderRepository);
        tableGroupEventListener = new TableGroupEventListener(orderTableRepository, orderStatusValidator);
    }

    @DisplayName("주문 테이블 그룹화")
    @Nested
    class TableGroupCreation {
        @DisplayName("잘못된 OrderTableId 가 있으면 실패한다.")
        @Test
        void fail_if_orderTables_contains_invalid_orderTableId() {
            // given
            final OrderTable orderTable = saveEmptyOrderTable();
            final Long invalidOrderTableId = -1L;
            final TableGroup tableGroup = saveTableGroup();
            final TableGroupCreateRequestEvent event =
                    new TableGroupCreateRequestEvent(List.of(orderTable.getId(), invalidOrderTableId), tableGroup);

            // then
            assertThatThrownBy(() -> tableGroupEventListener.handleTableGroupCreationRequested(event))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("착석되어있는 OrderTable 이 존재하면 실패한다.")
        @Test
        void fail_orderTables_contains_not_empty_orderTable() {
            // given
            final OrderTable orderTable1 = saveEmptyOrderTable();
            final OrderTable orderTable2 = saveOccupiedOrderTable();
            final List<Long> orderTableIdsInput = List.of(orderTable1.getId(), orderTable2.getId());
            final TableGroup tableGroup = saveTableGroup();
            final TableGroupCreateRequestEvent event = new TableGroupCreateRequestEvent(orderTableIdsInput, tableGroup);

            // then
            assertThatThrownBy(() -> tableGroupEventListener.handleTableGroupCreationRequested(event))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("이미 그룹 지정된 OrderTable 이 존재하면 실패한다.")
        @Test
        void fail_orderTables_contains_already_grouping() {
            // given
            final OrderTable orderTable1 = saveEmptyOrderTable();
            final OrderTable orderTable2 = saveEmptyOrderTable();
            saveTableGroup(List.of(orderTable1, orderTable2));
            final TableGroup tableGroup = saveTableGroup();

            final TableGroupCreateRequestEvent event =
                    new TableGroupCreateRequestEvent(List.of(orderTable1.getId(), orderTable2.getId()), tableGroup);

            // then
            assertThatThrownBy(() -> tableGroupEventListener.handleTableGroupCreationRequested(event))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 테이블 그룹 해제")
    @Nested
    class TableGroupDeletion {
        @DisplayName("주문 테이블 그룹의 주문들이 모두 계산 상태라면 그룹 해제에 성공한다.")
        @Test
        void success_if_all_orders_orderStatus_are_COMPLETION() {
            // given
            final OrderTable orderTable1 = saveEmptyOrderTable();
            final OrderTable orderTable2 = saveEmptyOrderTable();
            final TableGroup tableGroup = saveTableGroup(List.of(orderTable1, orderTable2));

            final Order order1 = saveOrder(orderTable1);
            order1.changeStatus(OrderStatus.COMPLETION);
            final Order order2 = saveOrder(orderTable2);
            order2.changeStatus(OrderStatus.COMPLETION);

            final TableGroupDeleteRequestEvent event = new TableGroupDeleteRequestEvent(tableGroup);

            // when
            tableGroupEventListener.handleTableGroupDeletionRequested(event);

            // then
            final List<OrderTable> updatedOrderTables = orderTableRepository.findAllById(
                    List.of(orderTable1.getId(), orderTable2.getId())
            );
            final List<Long> filteredOrderTablesId = updatedOrderTables.stream()
                    .filter(orderTable -> orderTable.getTableGroup() == null)
                    .filter(orderTable -> !orderTable.isEmpty())
                    .map(OrderTable::getId)
                    .collect(Collectors.toUnmodifiableList());

            assertThat(filteredOrderTablesId).containsExactly(orderTable1.getId(), orderTable2.getId());
        }

        @DisplayName("주문 테이블 그룹의 주문들 일부가 완료 상태가 아니라면 실패한다.")
        @Test
        void fail_if_some_orders_orderStatus_are_not_COMPLETION() {
            // given
            final OrderTable orderTable1 = saveEmptyOrderTable();
            final OrderTable orderTable2 = saveEmptyOrderTable();
            final TableGroup tableGroup = saveTableGroup(List.of(orderTable1, orderTable2));

            final Order order1 = saveOrder(orderTable1);
            order1.changeStatus(OrderStatus.MEAL);
            final Order order2 = saveOrder(orderTable2);
            order2.changeStatus(OrderStatus.COMPLETION);

            final TableGroupDeleteRequestEvent event = new TableGroupDeleteRequestEvent(tableGroup);

            // then
            assertThatThrownBy(() -> tableGroupEventListener.handleTableGroupDeletionRequested(event))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
