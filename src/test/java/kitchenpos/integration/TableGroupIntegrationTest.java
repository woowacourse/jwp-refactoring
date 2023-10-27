package kitchenpos.integration;

import kitchenpos.application.dto.OrderTableDto;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.domain.table.OrderTable;
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
        final OrderTable orderTable = new OrderTable(0, true);
        final HttpEntity<OrderTable> createTableRequest = new HttpEntity<>(orderTable);

        final OrderTable table1 = createTable(createTableRequest);
        final OrderTable table2 = createTable(createTableRequest);

        final List<OrderTableDto> orderTableIds = List.of(new OrderTableDto(table1.getId()), new OrderTableDto(table2.getId()));
        final HttpEntity<List<OrderTableDto>> request = new HttpEntity<>(orderTableIds);

        // when
        final ResponseEntity<TableGroupResponse> response = testRestTemplate
                .postForEntity("/api/table-groups", request, TableGroupResponse.class);
        final TableGroupResponse createdTableGroup = response.getBody();

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getHeaders().get("Location"))
                        .contains("/api/table-groups/" + createdTableGroup.getId()),
                () -> assertThat(createdTableGroup.getId()).isEqualTo(1L),
                () -> assertThat(createdTableGroup.getOrderTables()).hasSize(2),
                () -> assertThat(createdTableGroup.getOrderTables().get(0).isEmpty()).isFalse()
        );
    }

    @Test
    void 테이블_그룹을_해제요청한다() {
        // given
        final OrderTable orderTable = new OrderTable(0, true);
        final HttpEntity<OrderTable> createTableRequest = new HttpEntity<>(orderTable);

        final OrderTable table1 = createTable(createTableRequest);
        final OrderTable table2 = createTable(createTableRequest);

        final List<OrderTableDto> orderTableIds = List.of(new OrderTableDto(table1.getId()), new OrderTableDto(table2.getId()));
        final HttpEntity<List<OrderTableDto>> request = new HttpEntity<>(orderTableIds);

        final Long tableGroupId = testRestTemplate
                .postForEntity("/api/table-groups", request, TableGroupResponse.class)
                .getBody().getId();

        // when
        final ResponseEntity<Void> response = testRestTemplate
                .exchange("/api/table-groups/" + tableGroupId, HttpMethod.DELETE, null, Void.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    private OrderTable createTable(HttpEntity<OrderTable> request) {
        final OrderTableResponse response = testRestTemplate
                .postForEntity("/api/tables", request, OrderTableResponse.class)
                .getBody();

        return new OrderTable(response.getId(), null, response.getNumberOfGuests(), response.isEmpty());
    }
}
