package kitchenpos.tablegroup.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.enums.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.TableRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TableGroupServiceTest {
    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("create: 테이블 그룹 생성 테스트")
    @Test
    void createTest() {
        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(0, true));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(0, true));

        final TableGroupRequest tableGroupRequest = new TableGroupRequest(
                Arrays.asList(new TableRequest(orderTable1.getId()), new TableRequest(orderTable2.getId())));

        final TableGroupResponse saved = tableGroupService.create(tableGroupRequest);

        assertThat(saved.getOrderTables()).hasSize(2);
    }

    @DisplayName("create: 비어있지 않은 테이블이 있다면 예외처리")
    @Test
    void createTestByNotEmptyTable() {
        final OrderTable orderTable = new OrderTable(0, false);
        final OrderTable orderTable1 = orderTableRepository.save(orderTable);
        final OrderTable orderTable2 = orderTableRepository.save(orderTable);
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(
                Arrays.asList(new TableRequest(orderTable1.getId()), new TableRequest(orderTable2.getId())));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("입력한 테이블과 실제 테이블의 수가 다릅니다.");
    }

    @DisplayName("ungroup: 테이블 그룹 해제 테스트")
    @Test
    void ungroupTest() {
        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(0, true));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(0, true));
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(
                Arrays.asList(new TableRequest(orderTable1.getId()), new TableRequest(orderTable2.getId())));

        final TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroupResponse.getId()));
    }

    @DisplayName("ungroup: 테이블 상태가 Cocking이거나 Meal일 경우에는 예외처리")
    @Test
    void ungroupTestByStatusEqualsCokingAndMeal() {
        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(0, true));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(0, true));

        final TableGroupRequest tableGroupRequest = new TableGroupRequest(
                Arrays.asList(new TableRequest(orderTable1.getId()), new TableRequest(orderTable2.getId())));

        final TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        final Order order = new Order(orderTable1, OrderStatus.COOKING, new ArrayList<>());
        order.setOrderStatus(OrderStatus.COOKING);

        orderRepository.save(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupResponse.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
