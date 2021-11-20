package kitchenpos.acceptance.table;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.request.OrderTableRequest;
import kitchenpos.table.dto.request.TableGroupRequest;
import kitchenpos.table.dto.response.TableGroupResponse;
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
        OrderTable table = new OrderTable(null, 0, true);
        OrderTable savedTable = orderTableRepository.save(table);

        OrderTable table2 = new OrderTable(null, 0, true);
        OrderTable savedTable2 = orderTableRepository.save(table2);

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(
                new OrderTableRequest(savedTable.getId()),
                new OrderTableRequest(savedTable2.getId())
        ));

        ResponseEntity<TableGroupResponse> responseEntity = testRestTemplate.postForEntity(
                "/api/table-groups",
                tableGroupRequest,
                TableGroupResponse.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        TableGroupResponse response = responseEntity.getBody();
        assertThat(response.getId()).isEqualTo(1);
        assertThat(response.getOrderTableResponses()).hasSize(2);
    }

    @DisplayName("테이블 그룹 등록 실패 - 테이블 2개 미만")
    @Test
    void createByLessThanTwoTables() {
        OrderTable table = new OrderTable(null, 0, true);
        OrderTable savedTable = orderTableRepository.save(table);

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(
                new OrderTableRequest(savedTable.getId())
        ));

        ResponseEntity<TableGroupResponse> responseEntity = testRestTemplate.postForEntity(
                "/api/table-groups",
                tableGroupRequest,
                TableGroupResponse.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("테이블 그룹 등록 실패 - 잘못된 테이블 아이디")
    @Test
    void createByIncorrectTableId() {
        OrderTable table = new OrderTable(null, 0, true);
        OrderTable savedTable = orderTableRepository.save(table);

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(
                new OrderTableRequest(savedTable.getId()),
                new OrderTableRequest(100L)
        ));

        ResponseEntity<TableGroupResponse> responseEntity = testRestTemplate.postForEntity(
                "/api/table-groups",
                tableGroupRequest,
                TableGroupResponse.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("테이블 그룹 등록 실패 - 잘못된 테이블 상태")
    @Test
    void createByIncorrectTableState() {
        OrderTable table = new OrderTable(null, 0, true);
        OrderTable savedTable = orderTableRepository.save(table);
        OrderTable table2 = new OrderTable(null, 0, false);
        OrderTable savedTable2 = orderTableRepository.save(table2);

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(
                new OrderTableRequest(savedTable.getId()),
                new OrderTableRequest(savedTable2.getId())
        ));

        ResponseEntity<TableGroupResponse> responseEntity = testRestTemplate.postForEntity(
                "/api/table-groups",
                tableGroupRequest,
                TableGroupResponse.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("테이블 그룹 등록 실패 - 테이블 그룹 아이디가 있는 경우")
    @Test
    void createWhenTableHasTableGroup() {
        OrderTable table = new OrderTable(null, 0, true);
        OrderTable savedTable = orderTableRepository.save(table);

        TableGroup tableGroup = new TableGroup();
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        OrderTable table2 = new OrderTable(savedTableGroup.getId(), 0, true);
        OrderTable savedTable2 = orderTableRepository.save(table2);

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(
                new OrderTableRequest(savedTable.getId()),
                new OrderTableRequest(savedTable2.getId())
        ));

        ResponseEntity<TableGroupResponse> responseEntity = testRestTemplate.postForEntity(
                "/api/table-groups",
                tableGroupRequest,
                TableGroupResponse.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("테이블 그룹 해제 성공")
    @Test
    void ungroup() {
        TableGroup tableGroup = new TableGroup();
        tableGroup = tableGroupRepository.save(tableGroup);

        OrderTable table = new OrderTable(tableGroup.getId(), 0, true);
        OrderTable savedTable = orderTableRepository.save(table);

        OrderTable table2 = new OrderTable(tableGroup.getId(), 0, true);
        OrderTable savedTable2 = orderTableRepository.save(table2);

        testRestTemplate.delete("/api/table-groups/" + tableGroup.getId());

        savedTable = orderTableRepository.findById(savedTable.getId()).orElseThrow(IllegalArgumentException::new);
        assertThat(savedTable.getTableGroupId()).isNull();

        savedTable2 = orderTableRepository.findById(savedTable2.getId()).orElseThrow(IllegalArgumentException::new);
        assertThat(savedTable2.getTableGroupId()).isNull();
    }

    @DisplayName("테이블 그룹 해제 실패 - 조리 상태의 주문이 있는 경우")
    @Test
    void ungroupWhenOrderIsCooking() {
        TableGroup tableGroup = new TableGroup();
        tableGroup = tableGroupRepository.save(tableGroup);

        OrderTable table = new OrderTable(tableGroup.getId(), 0, true);
        orderTableRepository.save(table);

        OrderTable table2 = new OrderTable(tableGroup.getId(), 0, true);
        OrderTable savedTable2 = orderTableRepository.save(table2);

        Order order = new Order(savedTable2, OrderStatus.COOKING.name());
        orderRepository.save(order);

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
        tableGroup = tableGroupRepository.save(tableGroup);

        OrderTable table = new OrderTable(tableGroup.getId(), 0, true);
        orderTableRepository.save(table);

        OrderTable table2 = new OrderTable(tableGroup.getId(), 0, true);
        OrderTable savedTable2 = orderTableRepository.save(table2);

        Order order = new Order(savedTable2, OrderStatus.MEAL.name());
        orderRepository.save(order);

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/api/table-groups/" + tableGroup.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
