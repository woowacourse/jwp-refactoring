package kitchenpos.acceptance.tablegroup;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class TableGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("테이블 그룹 등록 성공")
    @Test
    void create() {
        OrderTable table = new OrderTable();
        table.setNumberOfGuests(0);
        table.setEmpty(true);
        OrderTable savedTable = orderTableDao.save(table);

        OrderTable table2 = new OrderTable();
        table2.setNumberOfGuests(0);
        table2.setEmpty(true);
        OrderTable savedTable2 = orderTableDao.save(table2);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(savedTable, savedTable2));

        ResponseEntity<TableGroup> responseEntity = testRestTemplate.postForEntity(
                "/api/table-groups",
                tableGroup,
                TableGroup.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        TableGroup response = responseEntity.getBody();
        assertThat(response.getId()).isEqualTo(1);
        assertThat(response.getOrderTables()).hasSize(2);
    }

    @DisplayName("테이블 그룹 등록 실패 - 테이블 2개 미만")
    @Test
    void createByLessThanTwoTables() {
        OrderTable table = new OrderTable();
        table.setNumberOfGuests(0);
        table.setEmpty(true);
        OrderTable savedTable = orderTableDao.save(table);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(savedTable));

        ResponseEntity<TableGroup> responseEntity = testRestTemplate.postForEntity(
                "/api/table-groups",
                tableGroup,
                TableGroup.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("테이블 그룹 등록 실패 - 잘못된 테이블 아이디")
    @Test
    void createByIncorrectTableId() {
        OrderTable table = new OrderTable();
        table.setNumberOfGuests(0);
        table.setEmpty(true);
        OrderTable savedTable = orderTableDao.save(table);

        OrderTable table2 = new OrderTable();
        table2.setId(100L);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(savedTable, table2));

        ResponseEntity<TableGroup> responseEntity = testRestTemplate.postForEntity(
                "/api/table-groups",
                tableGroup,
                TableGroup.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("테이블 그룹 등록 실패 - 잘못된 테이블 상태")
    @Test
    void createByIncorrectTableState() {
        OrderTable table = new OrderTable();
        table.setNumberOfGuests(0);
        table.setEmpty(true);
        OrderTable savedTable = orderTableDao.save(table);

        OrderTable table2 = new OrderTable();
        table2.setNumberOfGuests(0);
        table2.setEmpty(false);
        OrderTable savedTable2 = orderTableDao.save(table2);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(savedTable, savedTable2));

        ResponseEntity<TableGroup> responseEntity = testRestTemplate.postForEntity(
                "/api/table-groups",
                tableGroup,
                TableGroup.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("테이블 그룹 등록 실패 - 테이블 그룹 아이디가 있는 경우")
    @Test
    void createWhenTableHasTableGroup() {
        OrderTable table = new OrderTable();
        table.setNumberOfGuests(0);
        table.setEmpty(true);
        OrderTable savedTable = orderTableDao.save(table);

        OrderTable table2 = new OrderTable();
        table2.setNumberOfGuests(0);
        table2.setEmpty(true);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        table2.setTableGroupId(savedTableGroup.getId());
        OrderTable savedTable2 = orderTableDao.save(table2);

        TableGroup tableGroup2 = new TableGroup();
        tableGroup2.setOrderTables(Arrays.asList(savedTable, savedTable2));

        ResponseEntity<TableGroup> responseEntity = testRestTemplate.postForEntity(
                "/api/table-groups",
                tableGroup2,
                TableGroup.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("테이블 그룹 해제 성공")
    @Test
    void ungroup() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup = tableGroupDao.save(tableGroup);

        OrderTable table = new OrderTable();
        table.setNumberOfGuests(0);
        table.setEmpty(true);
        table.setTableGroupId(tableGroup.getId());
        OrderTable savedTable = orderTableDao.save(table);

        OrderTable table2 = new OrderTable();
        table2.setNumberOfGuests(0);
        table2.setEmpty(true);
        table2.setTableGroupId(tableGroup.getId());
        OrderTable savedTable2 = orderTableDao.save(table2);

        testRestTemplate.delete("/api/table-groups/" + tableGroup.getId());

        savedTable = orderTableDao.findById(savedTable.getId()).orElseThrow(IllegalArgumentException::new);
        assertThat(savedTable.getTableGroupId()).isNull();

        savedTable2 = orderTableDao.findById(savedTable2.getId()).orElseThrow(IllegalArgumentException::new);
        assertThat(savedTable2.getTableGroupId()).isNull();
    }

    @DisplayName("테이블 그룹 해제 실패 - 조리 상태의 주문이 있는 경우")
    @Test
    void ungroupWhenOrderIsCooking() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup = tableGroupDao.save(tableGroup);

        OrderTable table = new OrderTable();
        table.setNumberOfGuests(0);
        table.setEmpty(true);
        table.setTableGroupId(tableGroup.getId());
        orderTableDao.save(table);

        OrderTable table2 = new OrderTable();
        table2.setNumberOfGuests(0);
        table2.setEmpty(true);
        table2.setTableGroupId(tableGroup.getId());
        OrderTable savedTable2 = orderTableDao.save(table2);

        Order order = new Order();
        order.setOrderTableId(savedTable2.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/api/table-groups/" + tableGroup.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("테이블 그룹 해제 실패 - 식사 상태의 주문이 있는 경우")
    @Test
    void ungroupWhenOrderIsMeal() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup = tableGroupDao.save(tableGroup);

        OrderTable table = new OrderTable();
        table.setNumberOfGuests(0);
        table.setEmpty(true);
        table.setTableGroupId(tableGroup.getId());
        orderTableDao.save(table);

        OrderTable table2 = new OrderTable();
        table2.setNumberOfGuests(0);
        table2.setEmpty(true);
        table2.setTableGroupId(tableGroup.getId());
        OrderTable savedTable2 = orderTableDao.save(table2);

        Order order = new Order();
        order.setOrderTableId(savedTable2.getId());
        order.setOrderStatus(OrderStatus.MEAL.name());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/api/table-groups/" + tableGroup.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
