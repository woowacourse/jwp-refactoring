package kitchenpos.acceptance.fixture;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.http.HttpStatus;

public class TableGroupStepDefinition {

    public static long 테이블_그룹을_생성한다(
        final LocalDateTime createDate,
        final List<Long> orderTableIds) {

        List<OrderTable> orderTables = orderTableIds.stream()
            .map(orderTableId -> new OrderTable(orderTableId))
            .collect(Collectors.toList());

        TableGroup tableGroup = new TableGroup(createDate, orderTables);

        return RestAssured.given().log().all()
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(tableGroup)
            .when().log().all()
            .post("/api/table-groups")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract().body().jsonPath().getLong("id");
    }

    public static void 테이블_그룹을_해제한다(
        final long tableGroupId) {

        RestAssured.given().log().all()
            .when().log().all()
            .get("/api/table-groups/" + tableGroupId)
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract().body().jsonPath().getLong("id");
    }
}
