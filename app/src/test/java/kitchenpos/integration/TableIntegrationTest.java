package kitchenpos.integration;

import kitchenpos.table.application.dto.response.OrderTableResponse;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class TableIntegrationTest extends IntegrationTest {

    @Test
    void 주문_테이블을_생성을_요청한다() {
        // given
        final OrderTable orderTable = new OrderTable(0, true);
        final HttpEntity<OrderTable> request = new HttpEntity<>(orderTable);

        // when
        final ResponseEntity<OrderTableResponse> response = createTable(request);
        final OrderTableResponse createdOrderTable = response.getBody();

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getHeaders().get("Location"))
                        .contains("/api/tables/" + createdOrderTable.getId()),
                () -> assertThat(createdOrderTable.getTableGroupId()).isNull()
        );
    }

    @Test
    void 모든_주문_테이블을_조회한다() {
        // given
        final OrderTable orderTable = new OrderTable(0, true);
        final HttpEntity<OrderTable> request = new HttpEntity<>(orderTable);

        createTable(request);
        createTable(request);

        // when
        final ResponseEntity<OrderTable[]> response = testRestTemplate
                .getForEntity("/api/tables", OrderTable[].class);
        final List<OrderTable> orderTables = Arrays.asList(response.getBody());

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(orderTables).hasSize(2)
        );
    }

    @Test
    void 주문_테이블의_빈_상태를_수정요청한다() {
        // given
        final OrderTable orderTable = new OrderTable(0, true);
        final HttpEntity<OrderTable> request = new HttpEntity<>(orderTable);
        final Long orderTableId = createTable(request).getBody().getId();

        // when
        final OrderTable toUpdateTable = new OrderTable(0, false);
        final HttpEntity<OrderTable> updateRequest = new HttpEntity<>(toUpdateTable);

        final ResponseEntity<OrderTable> response = testRestTemplate
                .exchange("/api/tables/" + orderTableId + "/empty", HttpMethod.PUT, updateRequest, OrderTable.class);
        final OrderTable updatedOrderTable = response.getBody();

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(updatedOrderTable.isEmpty()).isFalse()
        );
    }

    @Test
    void 주문_테이블의_사용자_수를_수정요청한다() {
        // given
        final OrderTable orderTable = new OrderTable(1, false);
        final HttpEntity<OrderTable> request = new HttpEntity<>(orderTable);
        final Long orderTableId = createTable(request).getBody().getId();

        final OrderTable toUpdateTable = new OrderTable(10, false);
        final HttpEntity<OrderTable> updateRequest = new HttpEntity<>(toUpdateTable);

        // when
        final ResponseEntity<OrderTable> response = testRestTemplate
                .exchange("/api/tables/" + orderTableId + "/number-of-guests", HttpMethod.PUT, updateRequest, OrderTable.class);
        final OrderTable updatedOrderTable = response.getBody();

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(updatedOrderTable.getNumberOfGuests()).isEqualTo(10)
        );
    }

    private ResponseEntity<OrderTableResponse> createTable(HttpEntity<OrderTable> request) {
        return testRestTemplate
                .postForEntity("/api/tables", request, OrderTableResponse.class);
    }
}
