package kitchenpos.tablegroup.application;

import static java.util.stream.Collectors.toList;
import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.exception.InvalidOrderStatusException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.InvalidOrderTablesException;
import kitchenpos.table.exception.OrderTableNotEmptyException;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.exception.OrderTableNotFoundException;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import kitchenpos.tablegroup.ui.request.TableGroupRequest;
import kitchenpos.tablegroup.ui.response.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("TableGroup Service 테스트")
@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("TableGroup을 생성할 때")
    @Nested
    class CreateTableGroup {

        @DisplayName("TableGroup의 OrderTable의 개수가 2개 미만일 경우 예외가 발생한다.")
        @Test
        void orderTablesUnderTwoException() {
            // given
            OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(5, false));
            TableGroupRequest request = TableGroupRequest를_생성한다(savedOrderTable);

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isExactlyInstanceOf(InvalidOrderTablesException.class);
        }

        @DisplayName("TableGroup의 OrderTable들과 실제 저장된 OrderTable들이 일치하지 않을 경우 예외가 발생한다.")
        @Test
        void nonMatchOrderTablesException() {
            // given
            OrderTable orderTable1 = orderTableRepository.save(OrderTable을_생성한다(1));
            OrderTable orderTable2 = orderTableRepository.save(OrderTable을_생성한다(2));
            OrderTable orderTable3 = orderTableRepository.save(OrderTable을_생성한다(3));

            TableGroupRequest request = TableGroupRequest를_생성한다(orderTable1, orderTable2, orderTable3);
            orderTableRepository.delete(orderTable3);

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isExactlyInstanceOf(OrderTableNotFoundException.class);
        }

        @DisplayName("TableGroup의 OrderTable이 비어있는 상태가 아닐 경우 예외가 발생한다.")
        @Test
        void orderTableIsNotEmptyException() {
            // given
            OrderTable orderTable1 = orderTableRepository.save(OrderTable을_생성한다(1, false));
            OrderTable orderTable2 = orderTableRepository.save(OrderTable을_생성한다(2, false));
            OrderTable orderTable3 = orderTableRepository.save(OrderTable을_생성한다(3, false));

            TableGroupRequest request = TableGroupRequest를_생성한다(orderTable1, orderTable2, orderTable3);

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isExactlyInstanceOf(OrderTableNotEmptyException.class);
        }

        @DisplayName("TableGroup의 OrderTable에 이미 TableGroupId가 등록되어 있는 경우 예외가 발생한다.")
        @Test
        void alreadyMappingTableGroupException() {
            // given
            TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.create());
            OrderTable orderTable1 = orderTableRepository.save(OrderTable을_생성한다(1, savedTableGroup, true));
            OrderTable orderTable2 = orderTableRepository.save(OrderTable을_생성한다(2, savedTableGroup, true));
            OrderTable orderTable3 = orderTableRepository.save(OrderTable을_생성한다(3, savedTableGroup, true));

            TableGroupRequest request = TableGroupRequest를_생성한다(orderTable1, orderTable2, orderTable3);

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isExactlyInstanceOf(OrderTableNotEmptyException.class);
        }

        @DisplayName("TableGroup의 OrderTable이 정상적인 경우 TableGroup과 연결된다.")
        @Test
        void success() {
            // given
            OrderTable orderTable1 = orderTableRepository.save(OrderTable을_생성한다(1, true));
            OrderTable orderTable2 = orderTableRepository.save(OrderTable을_생성한다(2, true));
            OrderTable orderTable3 = orderTableRepository.save(OrderTable을_생성한다(3, true));

            TableGroupRequest request = TableGroupRequest를_생성한다(orderTable1, orderTable2, orderTable3);

            // when
            TableGroupResponse response = tableGroupService.create(request);

            // then
            assertThat(response.getId()).isNotNull();
        }
    }

    @DisplayName("TableGroup을 해제할 때")
    @Nested
    class Ungroup {

        @DisplayName("COOKING 중인 OrderTable이 포함된 Order가 존재하는 경우 예외가 발생한다.")
        @Test
        void existsByOrderTableIdInAndCOOKINGStatusInException() {
            // given
            OrderTable orderTable1 = orderTableRepository.save(OrderTable을_생성한다(1, true));
            OrderTable orderTable2 = orderTableRepository.save(OrderTable을_생성한다(2, true));

            TableGroupRequest request = TableGroupRequest를_생성한다(orderTable1, orderTable2);
            TableGroupResponse response = tableGroupService.create(request);

            Order order = orderRepository.save(Order를_생성한다(orderTable1.getId(), COOKING));

            // when, then
            assertThatThrownBy(() -> tableGroupService.ungroup(response.getId()))
                .isExactlyInstanceOf(InvalidOrderStatusException.class);
        }

        @DisplayName("MEAL 중인 OrderTable이 포함된 Order가 존재하는 경우 예외가 발생한다.")
        @Test
        void existsByOrderTableIdInAndMEALStatusInException() {
            // given
            OrderTable orderTable1 = orderTableRepository.save(OrderTable을_생성한다(1, true));
            OrderTable orderTable2 = orderTableRepository.save(OrderTable을_생성한다(2, true));

            TableGroupRequest request = TableGroupRequest를_생성한다(orderTable1, orderTable2);
            TableGroupResponse response = tableGroupService.create(request);

            Order order = orderRepository.save(Order를_생성한다(orderTable1.getId(), MEAL));

            // when, then
            assertThatThrownBy(() -> tableGroupService.ungroup(response.getId()))
                .isExactlyInstanceOf(InvalidOrderStatusException.class);
        }

        @DisplayName("COOKING, MEAL 중인 OrderTable이 포함된 Order가 존재하지 않는 경우 그룹 해제가 된다.")
        @Test
        void success() {
            // given
            OrderTable orderTable1 = orderTableRepository.save(OrderTable을_생성한다(1, true));
            OrderTable orderTable2 = orderTableRepository.save(OrderTable을_생성한다(2, true));

            TableGroupRequest request = TableGroupRequest를_생성한다(orderTable1, orderTable2);
            TableGroupResponse response = tableGroupService.create(request);

            Order order = orderRepository.save(Order를_생성한다(orderTable1.getId(), COMPLETION));

            // when
            tableGroupService.ungroup(response.getId());
            TableGroup ungroupedTableGroup = tableGroupService.findById(response.getId());
            List<OrderTable> ungroupedOrderTables = orderTableRepository.findAllByTableGroupId(ungroupedTableGroup.getId());

            // then
            for (OrderTable orderTable : ungroupedOrderTables) {
                assertThat(orderTable.getTableGroupId()).isNull();
                assertThat(orderTable.isEmpty()).isFalse();
            }
        }

    }
    private TableGroupRequest TableGroupRequest를_생성한다(OrderTable... orderTables) {
        List<Long> orderTableIds = Arrays.stream(orderTables)
            .map(OrderTable::getId)
            .collect(toList());

        return new TableGroupRequest(orderTableIds);
    }

    private OrderTable OrderTable을_생성한다(int numberOfGuests) {
        return OrderTable을_생성한다(numberOfGuests, false);
    }

    private OrderTable OrderTable을_생성한다(int numberOfGuests, boolean isEmpty) {
        return new OrderTable(numberOfGuests, isEmpty);
    }

    private OrderTable OrderTable을_생성한다(int numberOfGuests, TableGroup tableGroup, boolean isEmpty) {
        OrderTable orderTable = new OrderTable(numberOfGuests, isEmpty);
        orderTable.groupBy(tableGroup.getId());

        return orderTable;
    }

    private Order Order를_생성한다(Long orderTableId, OrderStatus orderStatus) {
        Order order = new Order(orderTableId);
        order.changeStatus(orderStatus);

        return order;
    }
}
