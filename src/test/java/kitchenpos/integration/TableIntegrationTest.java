package kitchenpos.integration;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

class TableIntegrationTest extends IntegrationTest {

    private static final String TABLE_URL = "/api/tables";

    @DisplayName("OrderTable 을 생성한다")
    @Test
    void create() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);

        // when
        ResponseEntity<OrderTable> orderTableResponseEntity = testRestTemplate.postForEntity(
                TABLE_URL,
                orderTable,
                OrderTable.class
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
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);
        Long orderTableId = orderTableDao.save(orderTable)
                .getId();

        // when
        ResponseEntity<OrderTable[]> orderTablesResponseEntity = testRestTemplate.getForEntity(
                TABLE_URL,
                OrderTable[].class
        );
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
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);
        Long orderTableId = orderTableDao.save(orderTable)
                .getId();

        // when
        orderTable.setEmpty(false);

        ResponseEntity<OrderTable> orderTableResponseEntity = testRestTemplate.exchange(
                TABLE_URL + "/{orderTableId}/empty",
                HttpMethod.PUT,
                new HttpEntity<>(orderTable),
                OrderTable.class,
                orderTableId
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
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(false);
        Long orderTableId = orderTableDao.save(orderTable)
                .getId();

        // when
        orderTable.setNumberOfGuests(4);

        ResponseEntity<OrderTable> orderTableResponseEntity = testRestTemplate.exchange(
                TABLE_URL + "/{orderTableId}/number-of-guests",
                HttpMethod.PUT,
                new HttpEntity<>(orderTable),
                OrderTable.class,
                orderTableId
        );
        HttpStatus statusCode = orderTableResponseEntity.getStatusCode();
        OrderTable body = orderTableResponseEntity.getBody();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.OK);
        assertThat(body).isNotNull();
        assertThat(body.getNumberOfGuests()).isEqualTo(4);
    }
}
