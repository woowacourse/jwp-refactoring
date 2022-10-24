package acceptance;

import static acceptance.TableAcceptanceTest.createTable;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.Application;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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
public class TableGroupAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("단체 지정을 저장한다.")
    @Test
    void saveTableGroup() {
        long table1 = createTable(2, true);
        long table2 = createTable(2, true);
        long table3 = createTable(3, true);

        List<Long> tableIds = List.of(table1, table2, table3);
        TableGroup tableGroup = givenTableGroup(tableIds);

        RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(tableGroup)
                .when().log().all()
                .post("/api/table-groups")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getLong("id");
    }

    private TableGroup givenTableGroup(List<Long> tableIds) {
        List<OrderTable> orderTables = tableIds.stream()
                .map(id -> {
                    OrderTable orderTable = new OrderTable();
                    orderTable.setId(id);
                    return orderTable;
                })
                .collect(Collectors.toList());
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }
}
