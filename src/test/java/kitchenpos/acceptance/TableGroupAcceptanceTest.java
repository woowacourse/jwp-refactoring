package kitchenpos.acceptance;

import static kitchenpos.acceptance.TableAcceptanceTest.createTable;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderTableRequest;
import kitchenpos.application.dto.TableGroupOrderTableRequest;
import kitchenpos.application.dto.TableGroupRequest;
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
        List<TableGroupOrderTableRequest> orderTableRequests = tableIds.stream()
                .map(TableGroupOrderTableRequest::new)
                .collect(Collectors.toList());
        TableGroupRequest tableGroup = new TableGroupRequest(orderTableRequests);

        RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(tableGroup)
                .when().log().all()
                .post("/api/table-groups")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }
}
