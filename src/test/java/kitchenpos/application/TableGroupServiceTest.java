package kitchenpos.application;

import kitchenpos.application.common.TestFixtureFactory;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.table.OrderTableResponse;
import kitchenpos.dto.table.TableGroupRequest;
import kitchenpos.dto.table.TableGroupResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/delete_all.sql")
class TableGroupServiceTest extends TestFixtureFactory {
    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("테이블 그룹 생성 메서드 테스트")
    @Test
    void create() {
        OrderTable orderTable1 = makeSavedOrderTable(0, true);
        OrderTable orderTable2 = makeSavedOrderTable(0, true);

        TableGroupRequest tableGroupRequest =
                makeTableGroupingRequest(Arrays.asList(orderTable1.getId(), orderTable2.getId()));
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        OrderTable changedOrderTable = orderTableDao.findById(orderTable1.getId()).get();
        assertAll(
                () -> assertThat(tableGroupResponse.getId()).isNotNull(),
                () -> assertThat(tableGroupResponse.getOrderTables()).hasSize(2),
                () -> assertThat(changedOrderTable.getTableGroup().getId()).isEqualTo(tableGroupResponse.getId())
        );
    }

    @DisplayName("테이블 그룹 생성 - 그룹을 맺으려는 테이블의 수가 2보다 작은 경우 예외 처리")
    @Test
    void createWithOrderTablesLessTwo() {
        OrderTable savedOrderTable1 = makeSavedOrderTable(0, true);

        TableGroupRequest tableGroupRequest =
                makeTableGroupingRequest(Arrays.asList(savedOrderTable1.getId()));


        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 생성 - 존재하지 않는 테이블의 아이디를 입력받은 경우 예외처리")
    @Test
    void createWithNotFoundOrderTable() {
        OrderTable savedOrderTable1 = makeSavedOrderTable(0, true);
        OrderTable savedOrderTable2 = makeSavedOrderTable(0, true);

        TableGroupRequest tableGroupRequest =
                makeTableGroupingRequest(Arrays.asList(savedOrderTable1.getId(), savedOrderTable2.getId() + 1));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 생성 - 이미 그룹핑된 테이블인 경우 예외처리")
    @Test
    void createWithGroupedTable() {
        OrderTable savedOrderTable1 = makeSavedOrderTable(0, true);
        OrderTable savedOrderTable2 = makeSavedOrderTable(0, true);
        OrderTable savedOrderTable3 = makeSavedOrderTable(0, true);

        TableGroupRequest tableGroupRequest =
                makeTableGroupingRequest(Arrays.asList(savedOrderTable1.getId(), savedOrderTable2.getId()));
        tableGroupService.create(tableGroupRequest);

        TableGroupRequest tableGroupRequest2 =
                makeTableGroupingRequest(Arrays.asList(savedOrderTable2.getId(), savedOrderTable3.getId()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해지하는 메서드 테스트")
    @Test
    void ungroup() {
        OrderTable savedOrderTable1 = makeSavedOrderTable(0, true);
        OrderTable savedOrderTable2 = makeSavedOrderTable(0, true);

        TableGroupRequest tableGroupRequest =
                makeTableGroupingRequest(Arrays.asList(savedOrderTable1.getId(), savedOrderTable2.getId()));
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        tableGroupService.ungroup(tableGroupResponse.getId());

        List<OrderTable> orderTablesByTableGroupId = orderTableDao.findAllByTableGroupId(tableGroupResponse.getId());
        assertAll(
                () -> assertThat(orderTablesByTableGroupId).hasSize(0)
        );
    }

    @DisplayName("테이블 그룹을 해지 - OrderStatus가 COOKING 혹은 MEAL 인 경우 예외처리")
    @ParameterizedTest
    @CsvSource({"COOKING", "MEAL"})
    void ungroupWhenCookingOrMeal(OrderStatus orderStatus) {
        OrderTable savedOrderTable1 = makeSavedOrderTable(0, true);
        OrderTable savedOrderTable2 = makeSavedOrderTable(0, true);

        TableGroupRequest tableGroupRequest =
                makeTableGroupingRequest(Arrays.asList(savedOrderTable1.getId(), savedOrderTable2.getId()));
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        Order order = new Order(savedOrderTable1, orderStatus, LocalDateTime.now(), new ArrayList<>());
        orderDao.save(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupResponse.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private TableGroupRequest makeTableGroupingRequest(List<Long> ids) {
        List<OrderTableResponse> orderTableDtos = ids.stream()
                .map(OrderTableResponse::new)
                .collect(Collectors.toList());
        return new TableGroupRequest(orderTableDtos);
    }

    @AfterEach
    void tearDown() {
        orderDao.deleteAll();
        orderTableDao.deleteAll();
        menuDao.deleteAll();
        tableGroupDao.deleteAll();
    }
}
