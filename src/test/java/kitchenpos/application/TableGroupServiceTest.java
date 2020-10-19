package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.dto.TableRequest;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TableGroupServiceTest {
    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("create: 테이블 그룹 생성 테스트")
    @Test
    void createTest() {
        final OrderTable orderTable = new OrderTable(0, true);
        final OrderTable orderTable1 = orderTableDao.save(orderTable);
        final OrderTable orderTable2 = orderTableDao.save(orderTable);

        final TableGroupRequest tableGroupRequest = new TableGroupRequest(
                Arrays.asList(new TableRequest(orderTable1.getId()), new TableRequest(orderTable2.getId())));

        final TableGroupResponse saved = tableGroupService.create(tableGroupRequest);

        assertThat(saved.getOrderTables()).hasSize(2);
    }

    @DisplayName("create: 비어있지 않은 테이블이 있다면 예외처리")
    @Test
    void createTestByNotEmptyTable() {
        final OrderTable orderTable = new OrderTable(0, false);
        final OrderTable orderTable1 = orderTableDao.save(orderTable);
        final OrderTable orderTable2 = orderTableDao.save(orderTable);
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(
                Arrays.asList(new TableRequest(orderTable1.getId()), new TableRequest(orderTable2.getId())));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비어있지 않은 테이블은 그룹으로 지정할 수 없습니다.");
    }

    // TODO: 2020/10/19 아래와 같은 테스트 방식이 괜찮은지 물어보기 
    @DisplayName("ungroup: 테이블 그룹 해제 테스트")
    @Test
    void ungroupTest() {
        final OrderTable orderTable = new OrderTable(0, true);
        final OrderTable orderTable1 = orderTableDao.save(orderTable);
        final OrderTable orderTable2 = orderTableDao.save(orderTable);
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(
                Arrays.asList(new TableRequest(orderTable1.getId()), new TableRequest(orderTable2.getId())));

        final TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);
        tableGroupService.ungroup(tableGroupResponse.getId());

        final OrderTable expected1 = orderTableDao.findById(orderTable1.getId())
                .orElseThrow(IllegalArgumentException::new);
        final OrderTable expected2 = orderTableDao.findById(orderTable2.getId())
                .orElseThrow(IllegalArgumentException::new);

        assertAll(
                () -> assertThat(expected1.getTableGroupId()).isNull(),
                () -> assertThat(expected2.getTableGroupId()).isNull()
        );
    }

    @DisplayName("ungroup: 테이블 상태가 Cocking이거나 Meal일 경우에는 예외처리")
    @Test
    void ungroupTestByStatusEqualsCokingAndMeal() {
        final OrderTable orderTable = new OrderTable(0, true);
        final OrderTable orderTable1 = orderTableDao.save(orderTable);
        final OrderTable orderTable2 = orderTableDao.save(orderTable);

        final TableGroupRequest tableGroupRequest = new TableGroupRequest(
                Arrays.asList(new TableRequest(orderTable1.getId()), new TableRequest(orderTable2.getId())));

        final TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        final Order order = new Order(orderTable1.getId(), new ArrayList<>());
        order.setOrderStatus("COOKING");

        orderDao.save(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupResponse.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
