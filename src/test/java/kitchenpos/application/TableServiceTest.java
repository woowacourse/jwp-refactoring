package kitchenpos.application;

import static kitchenpos.TestObjectFactory.createOrder;
import static kitchenpos.TestObjectFactory.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableRequest;
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

    @DisplayName("테이블 추가")
    @Test
    void create() {
        TableRequest request = createTableRequest(0, true);

        OrderTable savedTable = tableService.create(request);

        assertThat(savedTable.getId()).isNotNull();
    }

    @DisplayName("테이블 전체 조회")
    @Test
    void list() {
        TableRequest request = createTableRequest(0, true);
        tableService.create(request);
        tableService.create(request);

        List<OrderTable> list = tableService.list();

        assertThat(list).hasSize(2);
    }

    @DisplayName("주문 등록 불가 여부 변경")
    @Test
    void changeEmpty() {
        TableRequest table = createTableRequest(0, true);
        OrderTable savedTable = tableService.create(table);
        TableRequest request = createTableRequest(false);

        OrderTable changedTable = tableService.changeEmpty(savedTable.getId(), request);

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
        OrderTable savedTable1 = tableService.create(table);
        OrderTable savedTable2 = tableService.create(table);
        List<OrderTable> tables = Arrays.asList(savedTable1, savedTable2);
        TableGroup tableGroup = createTableGroup(tables);
        tableGroupService.create(tableGroup);

        TableRequest request = createTableRequest(false);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable1.getId(), request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 조리, 식사 중인 테이블의 주문 등록 불가 여부 변경")
    @Test
    void changeEmpty_Fail_With_TableInProgress() {
        TableRequest table = createTableRequest(0, true);
        OrderTable savedTable = tableService.create(table);

        Order order = createOrder(savedTable);
        orderDao.save(order);

        TableRequest request = createTableRequest(true);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수 변경")
    @Test
    void changeNumberOfGuests() {
        TableRequest table = createTableRequest(0, false);
        OrderTable savedTable = tableService.create(table);
        TableRequest request = createTableRequest(10);

        OrderTable changedTable = tableService
            .changeNumberOfGuests(savedTable.getId(), request);

        assertThat(changedTable.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
    }

    @DisplayName("[예외] 0보다 작은 수로 손님 수 변경")
    @Test
    void changeNumberOfGuests_Fail_With_InvalidNumberOfGuest() {
        TableRequest table = createTableRequest(0, true);
        OrderTable savedTable = tableService.create(table);
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
        OrderTable savedEmptyTable = tableService.create(emptyTable);

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