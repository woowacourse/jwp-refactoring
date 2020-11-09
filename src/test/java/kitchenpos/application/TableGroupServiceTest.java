package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;

@Import(TableGroupService.class)
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableGroupService tableGroupService;

    private OrderTable orderTable1;

    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = orderTableRepository.save(new OrderTable(0, true));
        orderTable2 = orderTableRepository.save(new OrderTable(0, true));
    }

    @DisplayName("create: 테이블 그룹 생성")
    @Test
    void create() {
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTable1.getId(), orderTable2.getId()));
        final TableGroupResponse actual = tableGroupService.create(tableGroupRequest);

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getOrderTableResponses()).hasSize(2)
        );
    }

    @DisplayName("create: 주문 테이블이 사이즈와 조회 사이즈가 같지 않을 때 예외 처리")
    @Test
    void create_IfOrderTablesNotSameFindOrderTables_Exception() {
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(0L, orderTable2.getId()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 주문 테이블이 비어 있을 때(empty=true) 예외 처리")
    @Test
    void create_IfOrderTableIsEmpty_Exception() {
        final OrderTable notEmptyOrderTable = orderTableRepository.save(new OrderTable(2, false));
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(notEmptyOrderTable.getId(), orderTable2.getId()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 주문 테이블에 주문 그룹이 존재할 때 예외 처리")
    @Test
    void create_IfTableGroupExistInOrderTable_Exception() {
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        final OrderTable orderTableHasTableGroupId = orderTableRepository.save(new OrderTable(0, true));
        orderTableHasTableGroupId.updateTableGroup(tableGroup);
        orderTableRepository.save(orderTableHasTableGroupId);

        final TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTableHasTableGroupId.getId(), orderTable2.getId()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("ungroup: 그룹 해제")
    @Test
    void ungroup() {
        final TableGroupResponse tableGroupResponse = tableGroupService.create(new TableGroupRequest(Arrays.asList(orderTable1.getId(), orderTable2.getId())));

        assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroupResponse.getId()));
    }

    @DisplayName("ungroup: 주문이 완료되지 않았을 때 예외 처리")
    @Test
    void ungroup_IfOrderStatusIsNotCompletion_Exception() {
        final TableGroupResponse tableGroupResponse = tableGroupService.create(new TableGroupRequest((Arrays.asList(orderTable1.getId(), orderTable2.getId()))));
        orderRepository.save(new Order(orderTable1, "MEAL", LocalDateTime.now()));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupResponse.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}