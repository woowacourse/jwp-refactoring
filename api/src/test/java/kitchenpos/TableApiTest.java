package kitchenpos;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.OrderTable;
import kitchenpos.menu.domain.OrderTableRepository;
import kitchenpos.menu.dto.OrderTableEmptyRequest;
import kitchenpos.menu.dto.OrderTableNumberOfGuestsRequest;
import kitchenpos.menu.dto.OrderTableRequest;
import kitchenpos.menu.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TableApiTest extends ApiTest {

    private static final String BASE_URL = "/api/tables";

    @Autowired
    private OrderTableRepository orderTableRepository;

    private List<OrderTable> orderTables;

    @Override
    @BeforeEach
    void setUp() throws SQLException {
        super.setUp();
        orderTables = new ArrayList<>();

        orderTables.add(orderTableRepository.save(new OrderTable(0, true)));
        orderTables.add(orderTableRepository.save(new OrderTable(0, false)));
        orderTables.add(orderTableRepository.save(new OrderTable(0, true)));
    }

    @DisplayName("주문 테이블 등록")
    @Test
    void postTable() {
        OrderTableRequest request = new OrderTableRequest(0, true);

        ResponseEntity<OrderTableResponse> responseEntity = testRestTemplate.postForEntity(
            BASE_URL,
            request,
            OrderTableResponse.class
        );
        OrderTableResponse response = responseEntity.getBody();

        OrderTableResponse expected = new OrderTableResponse(null, null,
            request.getNumberOfGuests(), request.isEmpty());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getId()).isNotNull();
        assertThat(response).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(expected);
    }

    @DisplayName("주문 테이블 조회")
    @Test
    void getTables() {
        ResponseEntity<OrderTableResponse[]> responseEntity = testRestTemplate.getForEntity(
            BASE_URL,
            OrderTableResponse[].class
        );
        OrderTableResponse[] response = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response).hasSameSizeAs(orderTables)
            .usingRecursiveFieldByFieldElementComparator()
            .containsAll(OrderTableResponse.listFrom(orderTables));
    }

    @DisplayName("주문 테이블 빈 상태 수정")
    @Test
    void putTableEmpty() {
        OrderTable orderTableToPut = orderTables.get(0);
        OrderTableEmptyRequest request = new OrderTableEmptyRequest(false);

        ResponseEntity<OrderTableResponse> responseEntity = testRestTemplate.exchange(
            BASE_URL + "/" + orderTableToPut.getId() + "/empty", HttpMethod.PUT,
            new HttpEntity<>(request, new HttpHeaders()), OrderTableResponse.class
        );
        OrderTableResponse response = responseEntity.getBody();
        OrderTableResponse expected = new OrderTableResponse(
            orderTableToPut.getId(),
            null,
            orderTableToPut.getNumberOfGuests().getValue(),
            false
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response).usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @DisplayName("주문 테이블 방문한 손님 수 수정")
    @Test
    void putTableNumberOfGuests() {
        OrderTable orderTableToPut = orderTables.get(1);
        int newNumberOfGuests = 4;
        OrderTableNumberOfGuestsRequest request = new OrderTableNumberOfGuestsRequest(
            newNumberOfGuests
        );

        ResponseEntity<OrderTableResponse> responseEntity = testRestTemplate.exchange(
            BASE_URL + "/" + orderTableToPut.getId() + "/number-of-guests",
            HttpMethod.PUT, new HttpEntity<>(request, new HttpHeaders()), OrderTableResponse.class
        );
        OrderTableResponse response = responseEntity.getBody();
        OrderTableResponse expected = new OrderTableResponse(
            orderTableToPut.getId(),
            null,
            newNumberOfGuests,
            orderTableToPut.isEmpty()
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response).usingRecursiveComparison()
            .isEqualTo(expected);
    }
}
