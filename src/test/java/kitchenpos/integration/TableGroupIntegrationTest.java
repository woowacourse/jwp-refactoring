package kitchenpos.integration;

import kitchenpos.application.dto.OrderTableDto;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
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
        final OrderTable orderTable = new OrderTable(0, true);
        final HttpEntity<OrderTable> createTableRequest = new HttpEntity<>(orderTable);

        final OrderTable table1 = createTable(createTableRequest).getBody();
        final OrderTable table2 = createTable(createTableRequest).getBody();

        final List<OrderTableDto> orderTableIds = List.of(new OrderTableDto(table1.getId()), new OrderTableDto(table2.getId()));
        final HttpEntity<List<OrderTableDto>> request = new HttpEntity<>(orderTableIds);

        // when
        final ResponseEntity<TableGroup> response = testRestTemplate
                .postForEntity("/api/table-groups", request, TableGroup.class);
        final TableGroup createdTableGroup = response.getBody();

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getHeaders().get("Location"))
                        .contains("/api/table-groups/" + createdTableGroup.getId())
        );
    }

    @Test
    void 테이블_그룹을_삭제요청한다() {
        // given
        final OrderTable orderTable = new OrderTable(0, true);
        final HttpEntity<OrderTable> createTableRequest = new HttpEntity<>(orderTable);

        final OrderTable table1 = createTable(createTableRequest).getBody();
        final OrderTable table2 = createTable(createTableRequest).getBody();

        final List<OrderTableDto> orderTableIds = List.of(new OrderTableDto(table1.getId()), new OrderTableDto(table2.getId()));
        final HttpEntity<List<OrderTableDto>> request = new HttpEntity<>(orderTableIds);

        final Long tableGroupId = testRestTemplate
                .postForEntity("/api/table-groups", request, TableGroup.class)
                .getBody().getId();

        // when
        final ResponseEntity<Void> response = testRestTemplate
                .exchange("/api/table-groups/" + tableGroupId, HttpMethod.DELETE, null, Void.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    private ResponseEntity<OrderTable> createTable(HttpEntity<OrderTable> request) {
        return testRestTemplate
                .postForEntity("/api/tables", request, OrderTable.class);
    }

    private void changeOrderStatus(final Long orderId) {
        final HttpEntity<OrderStatus> updateRequest = new HttpEntity<>(OrderStatus.COOKING);
        testRestTemplate.exchange("/api/orders/" + orderId + "/order-status", HttpMethod.PUT, updateRequest, Order.class);
    }
}
