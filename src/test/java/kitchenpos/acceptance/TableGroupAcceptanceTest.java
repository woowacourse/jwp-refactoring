package kitchenpos.acceptance;

import static kitchenpos.acceptance.TableAcceptanceTest.createTable;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderTableRequest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class TableGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("단체 지정을 저장한다.")
    @Test
    void saveTableGroup() {
        long table1 = createTable(new OrderTableRequest(2, true));
        long table2 = createTable(new OrderTableRequest(2, true));
        long table3 = createTable(new OrderTableRequest(3, true));

        List<Long> tableIds = List.of(table1, table2, table3);
        List<OrderTable> orderTables = tableIds.stream()
                .map(id -> new OrderTable(id, null, 0, true))
                .collect(Collectors.toList());
        TableGroup tableGroup = TableGroup.create(LocalDateTime.now(), orderTables);

        RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(tableGroup)
                .when().log().all()
                .post("/api/table-groups")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getLong("id");
    }
}
