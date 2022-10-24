package acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.util.List;
import kitchenpos.Application;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT,
        classes = Application.class
)
public class TableAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("테이블 목록을 조회한다.")
    @Test
    void findTables() {
        // given
        long table1 = createTable(0, true);
        long table2 = createTable(2, false);
        long table3 = createTable(3, false);

        // when
        List<OrderTable> tables = getTables();

        // then
        assertThat(tables).extracting(OrderTable::getId, OrderTable::getNumberOfGuests, OrderTable::isEmpty)
                .containsExactlyInAnyOrder(
                        tuple(table1, 0, true),
                        tuple(table2, 2, false),
                        tuple(table3, 3, false)
                );
    }

    private long createTable(int numberOfGuests, boolean empty) {
        OrderTable table = givenTable(numberOfGuests, empty);
        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(table)
                .when().log().all()
                .post("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getLong("id");
    }

    private OrderTable givenTable(final int numberOfGuests, final boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    private List<OrderTable> getTables() {
        return RestAssured.given().log().all()
                .when().log().all()
                .get("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getList(".", OrderTable.class);
    }
}
