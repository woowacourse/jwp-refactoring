package kitchenpos.acceptance;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.request.OrderTableRequest;
import kitchenpos.application.request.TableGroupRequest;
import kitchenpos.application.response.OrderTableResponse;
import kitchenpos.application.response.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class TableGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("새로운 테이블을 그룹을 지어서 단체 지정할 수 있다.")
    @Test
    void create() {
        // given
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(LocalDateTime.now(), getOrderTableRequests());

        // when
        final TableGroupResponse response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(tableGroupRequest)
                .when().log().all()
                .post("/api/table-groups")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(TableGroupResponse.class);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getOrderTables())
                .hasSize(8)
                .extracting(OrderTableResponse::getNumberOfGuests, OrderTableResponse::isEmpty)
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
        final OrderTableResponse orderTable1 = saveOrderTable(0, true);
        final OrderTableResponse orderTable2 = saveOrderTable(0, true);
        final Long savedTableGroupId = saveTableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));

        // when & then
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .pathParam("tableGroupId", savedTableGroupId)
                .when().log().all()
                .delete("/api/table-groups/{tableGroupId}")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private static List<OrderTableRequest> getOrderTableRequests() {
        final List<OrderTableResponse> orderTableResponses = RestAssured.given().log().all()
                .when().log().all()
                .get("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList(".", OrderTableResponse.class);

        return orderTableResponses.stream()
                .map(it -> new OrderTableRequest(it.getId(), it.getTableGroupId(), it.getNumberOfGuests(),
                        it.isEmpty()))
                .collect(toList());
    }

    private static Long saveTableGroup(final LocalDateTime createdTime, final List<OrderTableResponse> orderTables) {
        final TableGroupRequest tableGroupRequest = makeTableGroupRequest(createdTime, orderTables);

        final TableGroupResponse tableGroupResponse = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(tableGroupRequest)
                .when().log().all()
                .post("/api/table-groups")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(TableGroupResponse.class);

        return tableGroupResponse.getId();
    }

    private static TableGroupRequest makeTableGroupRequest(final LocalDateTime createdTime,
                                                           final List<OrderTableResponse> orderTables) {
        final List<OrderTableRequest> orderTableRequests = orderTables.stream()
                .map(it -> new OrderTableRequest(it.getId(), it.getTableGroupId(),
                        it.getNumberOfGuests(), it.isEmpty()))
                .collect(toList());

        return new TableGroupRequest(createdTime, orderTableRequests);
    }

    private static OrderTableResponse saveOrderTable(final int numberOfGuests, final boolean empty) {
        final OrderTableRequest orderTableRequest = new OrderTableRequest(numberOfGuests, empty);

        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(orderTableRequest)
                .when().log().all()
                .post("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(OrderTableResponse.class);
    }
}
