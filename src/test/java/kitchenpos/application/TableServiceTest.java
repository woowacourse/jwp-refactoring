package kitchenpos.application;

import static kitchenpos.TestObjectFactory.createOrderTableIdRequest;
import static kitchenpos.TestObjectFactory.createTableGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableIdRequest;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableRequest;
import kitchenpos.dto.TableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/deleteAll.sql")
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao tableDao;

    @DisplayName("테이블 추가")
    @Test
    void create() {
        TableRequest request = createTableRequest(0, true);

        TableResponse savedTable = tableService.create(request);

        assertThat(savedTable.getId()).isNotNull();
    }

    @DisplayName("테이블 전체 조회")
    @Test
    void list() {
        TableRequest request = createTableRequest(0, true);
        tableService.create(request);
        tableService.create(request);

        List<TableResponse> list = tableService.list();

        assertThat(list).hasSize(2);
    }

    @DisplayName("주문 등록 불가 여부 변경")
    @Test
    void changeEmpty() {
        TableRequest table = createTableRequest(0, true);
        TableResponse savedTable = tableService.create(table);
        TableRequest request = createTableRequest(false);

        TableResponse changedTable = tableService.changeEmpty(savedTable.getId(), request);

        assertThat(changedTable.isEmpty()).isEqualTo(request.getEmpty());
    }

    @DisplayName("[예외] 존재하지 않는 테이블의 주문 등록 불가 여부 변경")
    @Test
    void changeEmpty_Fail_With_NotExistTable() {
        TableRequest request = createTableRequest(false);

        assertThatThrownBy(() -> tableService.changeEmpty(1000L, request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 그룹에 포함된 테이블의 주문 등록 불가 여부 변경")
    @Test
    void changeEmpty_Fail_With_TableInGroup() {
        TableRequest table = createTableRequest(0, true);
        TableResponse savedTable1 = tableService.create(table);
        TableResponse savedTable2 = tableService.create(table);
        OrderTableIdRequest tableIdRequest1 = createOrderTableIdRequest(savedTable1.getId());
        OrderTableIdRequest tableIdRequest2 = createOrderTableIdRequest(savedTable2.getId());
        List<OrderTableIdRequest> tables = Arrays.asList(tableIdRequest1, tableIdRequest2);
        TableGroupRequest tableGroup = createTableGroupRequest(tables);
        tableGroupService.create(tableGroup);

        TableRequest request = createTableRequest(false);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable1.getId(), request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 조리, 식사 중인 테이블의 주문 등록 불가 여부 변경")
    @Test
    void changeEmpty_Fail_With_TableInProgress() {
        TableRequest table = createTableRequest(0, true);
        TableResponse tableResponse = tableService.create(table);

        OrderTable savedTable = tableDao.findById(tableResponse.getId()).get();
        Order order = Order.builder()
            .orderTable(savedTable)
            .orderStatus(OrderStatus.COOKING.name())
            .orderedTime(LocalDateTime.now())
            .build();
        orderDao.save(order);

        TableRequest request = createTableRequest(true);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수 변경")
    @Test
    void changeNumberOfGuests() {
        TableRequest table = createTableRequest(0, false);
        TableResponse savedTable = tableService.create(table);
        TableRequest request = createTableRequest(10);

        TableResponse changedTable = tableService
            .changeNumberOfGuests(savedTable.getId(), request);

        assertThat(changedTable.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
    }

    @DisplayName("[예외] 0보다 작은 수로 손님 수 변경")
    @Test
    void changeNumberOfGuests_Fail_With_InvalidNumberOfGuest() {
        TableRequest table = createTableRequest(0, true);
        TableResponse savedTable = tableService.create(table);
        TableRequest request = createTableRequest(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 존재하지 않는 테이블의 손님 수 변경")
    @Test
    void changeNumberOfGuests_Fail_With_NotExistTable() {
        TableRequest request = createTableRequest(100);

        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(1000L, request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 빈 테이블의 손님 수 변경")
    @Test
    void changeNumberOfGuests_Fail_With_EmptyTable() {
        TableRequest emptyTable = createTableRequest(0, true);
        TableResponse savedEmptyTable = tableService.create(emptyTable);

        TableRequest request = createTableRequest(100);

        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(savedEmptyTable.getId(), request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private TableRequest createTableRequest(int numberOfGuests, boolean empty) {
        return TableRequest.builder()
            .numberOfGuests(numberOfGuests)
            .empty(empty)
            .build();
    }

    private TableRequest createTableRequest(boolean empty) {
        return TableRequest.builder()
            .numberOfGuests(null)
            .empty(empty)
            .build();
    }

    private TableRequest createTableRequest(int numberOfGuests) {
        return TableRequest.builder()
            .numberOfGuests(numberOfGuests)
            .empty(null)
            .build();
    }
}