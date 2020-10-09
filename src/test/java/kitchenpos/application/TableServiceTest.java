package kitchenpos.application;

import static kitchenpos.TestObjectFactory.createOrder;
import static kitchenpos.TestObjectFactory.createTable;
import static kitchenpos.TestObjectFactory.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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
        OrderTable table = createTable(true);
        OrderTable savedTable = tableService.create(table);

        assertThat(savedTable.getId()).isNotNull();
    }

    @DisplayName("테이블 전체 조회")
    @Test
    void list() {
        OrderTable table = createTable(true);
        tableService.create(table);
        tableService.create(table);

        List<OrderTable> list = tableService.list();

        assertThat(list).hasSize(2);
    }

    @DisplayName("주문 등록 불가 여부 변경")
    @Test
    void changeEmpty() {
        OrderTable table = createTable(true);
        OrderTable savedTable = tableService.create(table);
        OrderTable targetTable = createTable(false);

        OrderTable changedTable = tableService.changeEmpty(savedTable.getId(), targetTable);

        assertThat(changedTable.isEmpty()).isEqualTo(targetTable.isEmpty());
    }

    @DisplayName("[예외] 존재하지 않는 테이블의 주문 등록 불가 여부 변경")
    @Test
    void changeEmpty_Fail_With_NotExistTable() {
        OrderTable notSavedTable = OrderTable.builder()
            .id(1000L)
            .numberOfGuests(0)
            .empty(false)
            .build();
        OrderTable targetTable = createTable(false);

        assertThatThrownBy(() -> tableService.changeEmpty(notSavedTable.getId(), targetTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 그룹에 포함된 테이블의 주문 등록 불가 여부 변경")
    @Test
    void changeEmpty_Fail_With_TableInGroup() {
        OrderTable table1 = createTable(true);
        OrderTable savedTable1 = tableService.create(table1);

        OrderTable table2 = createTable(true);
        OrderTable savedTable2 = tableService.create(table2);
        List<OrderTable> tables = Arrays.asList(savedTable1, savedTable2);
        TableGroup tableGroup = createTableGroup(tables);
        tableGroupService.create(tableGroup);

        OrderTable targetTable = createTable(false);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable1.getId(), targetTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 조리, 식사 중인 테이블의 주문 등록 불가 여부 변경")
    @Test
    void changeEmpty_Fail_With_TableInProgress() {
        OrderTable table = createTable(false);
        OrderTable savedTable = tableService.create(table);

        Order order = createOrder(savedTable);
        orderDao.save(order);

        OrderTable targetTable = createTable(true);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), targetTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수 변경")
    @Test
    void changeNumberOfGuests() {
        OrderTable table = createTable(false);
        OrderTable savedTable = tableService.create(table);
        OrderTable targetTable = OrderTable.builder()
            .numberOfGuests(10)
            .build();

        OrderTable changedTable = tableService
            .changeNumberOfGuests(savedTable.getId(), targetTable);

        assertThat(changedTable.getNumberOfGuests()).isEqualTo(targetTable.getNumberOfGuests());
    }

    @DisplayName("[예외] 0보다 작은 수로 손님 수 변경")
    @Test
    void changeNumberOfGuests_Fail_With_InvalidNumberOfGuest() {
        OrderTable table = createTable(false);
        OrderTable savedTable = tableService.create(table);
        OrderTable targetTable = OrderTable.builder()
            .numberOfGuests(-1)
            .build();

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), targetTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 존재하지 않는 테이블의 손님 수 변경")
    @Test
    void changeNumberOfGuests_Fail_With_NotExistTable() {
        OrderTable notSavedTable = OrderTable.builder()
            .id(1000L)
            .numberOfGuests(0)
            .empty(false)
            .build();

        OrderTable targetTable = OrderTable.builder()
            .numberOfGuests(10)
            .build();

        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(notSavedTable.getId(), targetTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 빈 테이블의 손님 수 변경")
    @Test
    void changeNumberOfGuests_Fail_With_EmptyTable() {
        OrderTable emptyTable = createTable(true);
        OrderTable savedEmptyTable = tableService.create(emptyTable);
        OrderTable targetTable = OrderTable.builder()
            .numberOfGuests(10)
            .build();

        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(savedEmptyTable.getId(), targetTable))
            .isInstanceOf(IllegalArgumentException.class);
    }
}