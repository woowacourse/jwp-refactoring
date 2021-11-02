package kitchenpos.integration;

import static kitchenpos.integration.templates.OrderTableTemplate.TABLE_URL;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.factory.OrderTableFactory;
import kitchenpos.integration.annotation.IntegrationTest;
import kitchenpos.integration.templates.OrderTableTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@IntegrationTest
class TableIntegrationTest {

    @Autowired
    private OrderTableTemplate orderTableTemplate;

    @DisplayName("OrderTable 을 생성한다")
    @Test
    void create() {
        // given // when
        ResponseEntity<OrderTableResponse> orderTableResponseEntity = orderTableTemplate
            .create(
                0,
                true
            );
        HttpStatus statusCode = orderTableResponseEntity.getStatusCode();
        URI location = orderTableResponseEntity.getHeaders().getLocation();
        OrderTableResponse body = orderTableResponseEntity.getBody();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.CREATED);
        assertThat(body).isNotNull();
        assertThat(body.getId()).isNotNull();
        assertThat(body.getTableGroupId()).isNull();
        assertThat(body.getNumberOfGuests()).isZero();
        assertThat(body.isEmpty()).isTrue();
        assertThat(location).isEqualTo(URI.create(TABLE_URL + "/" + body.getId()));
    }

    @DisplayName("모든 OrderTable 을 조회한다")
    @Test
    void list() {
        // given
        OrderTableResponse orderTableResponse = orderTableTemplate
            .create(
                0,
                true)
            .getBody();
        assertThat(orderTableResponse).isNotNull();
        Long orderTableId = orderTableResponse.getId();

        // when
        ResponseEntity<OrderTable[]> orderTablesResponseEntity = orderTableTemplate
            .list();
        HttpStatus statusCode = orderTablesResponseEntity.getStatusCode();
        OrderTable[] body = orderTablesResponseEntity.getBody();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.OK);
        assertThat(body)
            .hasSize(1)
            .extracting("id")
            .contains(orderTableId);
    }


    @DisplayName("OrderTable 의 비어있는 상태를 변경한다")
    @Test
    void changeEmpty() {
        // given
        OrderTableResponse orderTableResponse = orderTableTemplate
            .create(
                0,
                true)
            .getBody();
        assertThat(orderTableResponse).isNotNull();
        OrderTable orderTable = OrderTableFactory.copy(orderTableResponse);
        Long orderTableId = orderTable.getId();

        // when
        OrderTable updatedOrderTable = OrderTableFactory.copy(orderTable)
            .empty(false)
            .build();

        ResponseEntity<OrderTable> orderTableResponseEntity = orderTableTemplate
            .changeEmpty(
                orderTableId,
                updatedOrderTable
            );
        HttpStatus statusCode = orderTableResponseEntity.getStatusCode();
        OrderTable body = orderTableResponseEntity.getBody();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.OK);
        assertThat(body).isNotNull();
        assertThat(body.isEmpty()).isFalse();
    }

    @DisplayName("OrderTable 에 있는 손님의 수를 변경한다")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTableResponse orderTableResponse = orderTableTemplate
            .create(
                0,
                false)
            .getBody();
        assertThat(orderTableResponse).isNotNull();
        OrderTable orderTable = OrderTableFactory.copy(orderTableResponse);
        Long orderTableId = orderTable.getId();

        // when
        OrderTable updatedOrderTable = OrderTableFactory.copy(orderTable)
            .numberOfGuests(10)
            .build();

        ResponseEntity<OrderTable> orderTableResponseEntity = orderTableTemplate
            .changeNumberOfGuests(
                orderTableId,
                updatedOrderTable
            );
        HttpStatus statusCode = orderTableResponseEntity.getStatusCode();
        OrderTable body = orderTableResponseEntity.getBody();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.OK);
        assertThat(body).isNotNull();
        assertThat(body.getNumberOfGuests()).isEqualTo(10);
    }
}
