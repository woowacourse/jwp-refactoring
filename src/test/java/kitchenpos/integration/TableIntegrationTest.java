package kitchenpos.integration;

import static kitchenpos.integration.templates.OrderTableTemplate.TABLE_URL;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import kitchenpos.domain.OrderTable;
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
        ResponseEntity<OrderTable> orderTableResponseEntity = orderTableTemplate
            .create(
                0,
                true
            );
        HttpStatus statusCode = orderTableResponseEntity.getStatusCode();
        URI location = orderTableResponseEntity.getHeaders().getLocation();
        OrderTable body = orderTableResponseEntity.getBody();

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
        OrderTable createdOrderTable = orderTableTemplate
            .create(
                0,
                true)
            .getBody();
        assertThat(createdOrderTable).isNotNull();
        Long createdOrderTableId = createdOrderTable.getId();

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
            .contains(createdOrderTableId);
    }


    @DisplayName("OrderTable 의 비어있는 상태를 변경한다")
    @Test
    void changeEmpty() {
        // given
        OrderTable orderTable = orderTableTemplate
            .create(
                0,
                true)
            .getBody();
        assertThat(orderTable).isNotNull();
        Long orderTableId = orderTable.getId();

        // when
        orderTable.setEmpty(false);

        ResponseEntity<OrderTable> orderTableResponseEntity = orderTableTemplate
            .changeEmpty(
                orderTableId,
                orderTable
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
        OrderTable orderTable = orderTableTemplate
            .create(
                0,
                false)
            .getBody();
        assertThat(orderTable).isNotNull();
        Long orderTableId = orderTable.getId();

        // when
        orderTable.setNumberOfGuests(4);

        ResponseEntity<OrderTable> orderTableResponseEntity = orderTableTemplate
            .changeNumberOfGuests(
                orderTableId,
                orderTable
            );
        HttpStatus statusCode = orderTableResponseEntity.getStatusCode();
        OrderTable body = orderTableResponseEntity.getBody();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.OK);
        assertThat(body).isNotNull();
        assertThat(body.getNumberOfGuests()).isEqualTo(4);
    }
}
