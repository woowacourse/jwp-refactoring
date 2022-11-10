package kitchenpos.application;

import static kitchenpos.fixture.OrderTableFixture.generateOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.FakeOrderDao;
import kitchenpos.dao.FakeOrderTableDao;
import kitchenpos.dao.FakeTableGroupDao;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableValidator;
import kitchenpos.table.dto.request.OrderTableRequest;
import kitchenpos.table.dto.request.TableGroupRequest;
import kitchenpos.table.dto.response.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableGroupServiceTest {

    private TableGroupService tableGroupService;

    private OrderDao orderDao;
    private OrderTableDao orderTableDao;
    private TableGroupDao tableGroupDao;
    private TableValidator tableValidator;

    @BeforeEach
    void beforeEach() {
        this.orderDao = new FakeOrderDao();
        this.orderTableDao = new FakeOrderTableDao();
        this.tableGroupDao = new FakeTableGroupDao();
        this.tableValidator = new TableValidator(orderDao);
        this.tableGroupService = new TableGroupService(orderTableDao, tableGroupDao, tableValidator);
    }

    @Test
    @DisplayName("테이블 그룹을 생성한다.")
    void create() {
        // given
        List<OrderTable> orderTables = new ArrayList<>();

        OrderTable orderTable1 = generateOrderTable(null, 0, true);
        OrderTable orderTable2 = generateOrderTable(null, 0, true);

        OrderTable saveOrderTable1 = orderTableDao.save(orderTable1);
        OrderTable saveOrderTable2 = orderTableDao.save(orderTable2);

        orderTables.add(saveOrderTable1);
        orderTables.add(saveOrderTable2);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        List<OrderTableRequest> orderTableRequests = orderTables.stream()
                .map(orderTable -> new OrderTableRequest(orderTable.getId()))
                .collect(Collectors.toList());
        TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableRequests);

        // when
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        // then
        assertAll(
                () -> assertThat(tableGroupResponse.getId()).isNotNull(),
                () -> assertThat(tableGroupResponse.getCreatedDate()).isNotNull(),
                () -> assertThat(tableGroupResponse.getOrderTables().size()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("테이블 그룹을 생성 시 주문테이블이 비어있다면 예외를 반환한다.")
    void create_WhenEmptyOrderTable() {
        // given
        List<OrderTable> orderTables = new ArrayList<>();

        OrderTable orderTable1 = generateOrderTable(null, 0, true);
        OrderTable orderTable2 = generateOrderTable(null, 0, true);

        OrderTable saveOrderTable1 = orderTableDao.save(orderTable1);

        orderTables.add(saveOrderTable1);
        List<OrderTableRequest> orderTableRequests = orderTables.stream()
                .map(orderTable -> new OrderTableRequest(orderTable.getId()))
                .collect(Collectors.toList());

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(orderTableRequests)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 생성 시 주문테이블이 1개 이하라면 예외를 반환한다.")
    void create_WhenUnderOneOrderTable() {
        // given
        List<OrderTableRequest> orderTableRequests = new ArrayList<>();

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(orderTableRequests)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블의 수는 최소 2개 이상이어야합니다.");
    }

    @Test
    @DisplayName("테이블 그룹을 생성 시 입력된 주문 테이블 목록과 저장된 주문 테이블 목록이 다르다면 예외를 반환한다.")
    void create_WhenNotExistOrderTable() {
        // given
        List<OrderTable> orderTables = new ArrayList<>();

        OrderTable orderTable1 = generateOrderTable(null, 0, true);
        OrderTable orderTable2 = generateOrderTable(null, 0, true);

        OrderTable saveOrderTable1 = orderTableDao.save(orderTable1);
        OrderTable saveOrderTable2 = orderTableDao.save(orderTable2);

        orderTables.add(saveOrderTable1);
        orderTables.add(saveOrderTable2);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        List<OrderTableRequest> orderTableRequests = orderTables.stream()
                .map(orderTable -> new OrderTableRequest(orderTable.getId()))
                .collect(Collectors.toList());
        orderTableRequests.add(new OrderTableRequest(3L));

        TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableRequests);
        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("요청 주문 테이블과 조회된 주문 테이블의 수는 같아야합니다.");
    }

    @Test
    @DisplayName("주문 테이블 목록과 테이블 그룹을 분리한다.")
    void ungroup() {
        // when
        List<OrderTable> orderTables = new ArrayList<>();

        OrderTable orderTable1 = generateOrderTable(null, 0, true);
        OrderTable orderTable2 = generateOrderTable(null, 0, true);

        OrderTable saveOrderTable1 = orderTableDao.save(orderTable1);
        OrderTable saveOrderTable2 = orderTableDao.save(orderTable2);

        orderTables.add(saveOrderTable1);
        orderTables.add(saveOrderTable2);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        List<OrderTableRequest> orderTableRequests = orderTables.stream()
                .map(orderTable -> new OrderTableRequest(orderTable.getId()))
                .collect(Collectors.toList());

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        List<OrderTable> orderTablesInTableGroup = orderTableDao.findAllByTableGroupId(tableGroup.getId());
        assertThat(orderTablesInTableGroup.size()).isEqualTo(0);
    }
}
