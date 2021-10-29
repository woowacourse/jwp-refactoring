package kitchenpos.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.generator.TableGenerator;
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
    private JdbcTemplateOrderTableDao orderTableDao;

    private List<OrderTable> orderTables;

    @Override
    @BeforeEach
    void setUp() throws SQLException {
        super.setUp();
        orderTables = new ArrayList<>();

        orderTables.add(orderTableDao.save(TableGenerator.newInstance(0, true)));
        orderTables.add(orderTableDao.save(TableGenerator.newInstance(0, false)));
        orderTables.add(orderTableDao.save(TableGenerator.newInstance(0, true)));
    }

    @DisplayName("주문 테이블 등록")
    @Test
    void postTable() {
        OrderTable request = TableGenerator.newInstance(0, true);

        ResponseEntity<OrderTable> responseEntity = testRestTemplate.postForEntity(BASE_URL, request, OrderTable.class);
        OrderTable response = responseEntity.getBody();

        OrderTable expected = TableGenerator.newInstance(request.getTableGroupId(), request.getNumberOfGuests(), request.isEmpty());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getId()).isNotNull();
        assertThat(response).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(expected);
    }

    @DisplayName("주문 테이블 조회")
    @Test
    void getTables() {
        ResponseEntity<OrderTable[]> responseEntity = testRestTemplate.getForEntity(BASE_URL, OrderTable[].class);
        OrderTable[] response = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response).hasSameSizeAs(orderTables)
            .usingRecursiveFieldByFieldElementComparator()
            .containsAll(orderTables);
    }

    @DisplayName("주문 테이블 빈 상태 수정")
    @Test
    void putTableEmpty() {
        OrderTable orderTableToPut = orderTables.get(0);
        OrderTable request = TableGenerator.newInstance(false);

        ResponseEntity<OrderTable> responseEntity = testRestTemplate.exchange(
            BASE_URL + "/" + orderTableToPut.getId() + "/empty", HttpMethod.PUT,
            new HttpEntity<>(request, new HttpHeaders()),
            OrderTable.class
        );
        OrderTable response = responseEntity.getBody();

        OrderTable expected = TableGenerator.newInstance(orderTableToPut.getId(), orderTableToPut.getTableGroupId(), orderTableToPut.getNumberOfGuests(), false);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response).usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @DisplayName("주문 테이블 방문한 손님 수 수정")
    @Test
    void putTableNumberOfGuests() {
        OrderTable orderTableToPut = orderTables.get(1);
        int newNumberOfGuests = 4;
        OrderTable request = TableGenerator.newInstance(newNumberOfGuests);

        ResponseEntity<OrderTable> responseEntity = testRestTemplate.exchange(
            BASE_URL + "/" + orderTableToPut.getId() + "/number-of-guests", HttpMethod.PUT,
            new HttpEntity<>(request, new HttpHeaders()),
            OrderTable.class
        );
        OrderTable response = responseEntity.getBody();

        OrderTable expected = TableGenerator.newInstance(orderTableToPut.getId(), orderTableToPut.getTableGroupId(), newNumberOfGuests,
            orderTableToPut.isEmpty());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
