package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class TableGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("새로운 테이블을 그룹을 지어서 단체 지정할 수 있다.")
    @Test
    void create() {
        // given
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), getOrderTables());

        // when
        final TableGroup response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(tableGroup)
                .when().log().all()
                .post("/api/table-groups")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(TableGroup.class);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getOrderTables())
                .hasSize(8)
                .extracting(OrderTable::getNumberOfGuests, OrderTable::isEmpty)
                .containsExactly(
                        tuple(0, false), tuple(0, false),
                        tuple(0, false), tuple(0, false),
                        tuple(0, false), tuple(0, false),
                        tuple(0, false), tuple(0, false)
                );
    }

    @DisplayName("지정된 테이블 그룹을 해제할 수 있다.")
    @Test
    void ungroup() {
        // given
        final OrderTable orderTable1 = saveOrderTable(0, true);
        final OrderTable orderTable2 = saveOrderTable(0, true);
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));
        final TableGroup savedTableGroup = saveTableGroup(tableGroup);

        // when & then
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .pathParam("tableGroupId", savedTableGroup.getId())
                .when().log().all()
                .delete("/api/table-groups/{tableGroupId}")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private static List<OrderTable> getOrderTables() {
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().log().all()
                .get("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        return getOrderTables(response);
    }

    private static List<OrderTable> getOrderTables(final ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", OrderTable.class);
    }

    private static TableGroup saveTableGroup(final TableGroup tableGroup) {
        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(tableGroup)
                .when().log().all()
                .post("/api/table-groups")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(TableGroup.class);
    }

    private static OrderTable saveOrderTable(final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = new OrderTable(numberOfGuests, empty);

        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().log().all()
                .post("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(OrderTable.class);
    }
}
