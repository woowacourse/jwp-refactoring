package kitchenpos.integration;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class TableGroupIntegrationTest extends IntegrationTest {

    @Test
    void 테이블_그룹_생성을_요청한다() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);
        final HttpEntity<OrderTable> createTableRequest = new HttpEntity<>(orderTable);

        final OrderTable table1 = createTable(createTableRequest).getBody();
        final OrderTable table2 = createTable(createTableRequest).getBody();

        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(table1, table2));
        final HttpEntity<TableGroup> request = new HttpEntity<>(tableGroup);

        // when
        final ResponseEntity<TableGroup> response = testRestTemplate
                .postForEntity("/api/table-groups", request, TableGroup.class);
        final TableGroup createdTableGroup = response.getBody();

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getHeaders().get("Location"))
                        .contains("/api/table-groups/" + createdTableGroup.getId()),
                () -> assertThat(createdTableGroup.getOrderTables()).hasSize(2)
        );
    }

    @Test
    void 테이블_그룹을_삭제요청한다() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);
        final HttpEntity<OrderTable> createTableRequest = new HttpEntity<>(orderTable);

        final OrderTable table1 = createTable(createTableRequest).getBody();
        final OrderTable table2 = createTable(createTableRequest).getBody();

        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(table1, table2));
        final HttpEntity<TableGroup> request = new HttpEntity<>(tableGroup);

        final Long tableGroupId = testRestTemplate
                .postForEntity("/api/table-groups", request, TableGroup.class)
                .getBody().getId();

        // when
        final ResponseEntity<Void> response = testRestTemplate
                .exchange("/api/table-groups/" + tableGroupId, HttpMethod.POST.DELETE, null, Void.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    private ResponseEntity<OrderTable> createTable(HttpEntity<OrderTable> request) {
        return testRestTemplate
                .postForEntity("/api/tables", request, OrderTable.class);
    }
}
