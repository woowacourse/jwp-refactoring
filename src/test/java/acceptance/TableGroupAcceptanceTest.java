package acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import common.AcceptanceTest;
import io.restassured.RestAssured;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.response.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class TableGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void createNewTableGroup() {
        // act
        TableGroup tableGroup = groupTables(1L, 2L);

        // assert
        assertThat(tableGroup.getId()).isNotNull();
        assertThat(tableGroup.getOrderTables())
                .extracting(OrderTable::getId)
                .contains(1L, 2L);

        List<OrderTableResponse> orderTables = getOrderTables();
        assertThat(orderTables)
                .extracting(
                        OrderTableResponse::getId, OrderTableResponse::getNumberOfGuests,
                        OrderTableResponse::isEmpty, OrderTableResponse::getTableGroupId
                )
                .containsExactlyInAnyOrder(
                        tuple(1L, 0, false, tableGroup.getId()),
                        tuple(2L, 0, false, tableGroup.getId()),
                        tuple(3L, 0, true, null),
                        tuple(4L, 0, true, null),
                        tuple(5L, 0, true, null),
                        tuple(6L, 0, true, null),
                        tuple(7L, 0, true, null),
                        tuple(8L, 0, true, null)
                );
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroupTables() {
        // arrange
        TableGroup tableGroup = groupTables(1L, 2L);

        // act
        ungroupTables(tableGroup.getId());

        // assert
        List<OrderTableResponse> orderTables = getOrderTables();
        assertThat(orderTables)
                .extracting(
                        OrderTableResponse::getId, OrderTableResponse::getNumberOfGuests,
                        OrderTableResponse::isEmpty, OrderTableResponse::getTableGroupId
                )
                .containsExactlyInAnyOrder(
                        tuple(1L, 0, false, null),
                        tuple(2L, 0, false, null),
                        tuple(3L, 0, true, null),
                        tuple(4L, 0, true, null),
                        tuple(5L, 0, true, null),
                        tuple(6L, 0, true, null),
                        tuple(7L, 0, true, null),
                        tuple(8L, 0, true, null)
                );
    }

    private void ungroupTables(Long groupId) {
        RestAssured.given().log().all()
                .pathParam("group-id", groupId)
                .when().log().all()
                .delete("/api/table-groups/{group-id}")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private TableGroup groupTables(long... ids) {
        List<Map<String, Long>> orderTables = Arrays.stream(ids)
                .mapToObj(id -> Map.of("id", id))
                .collect(Collectors.toList());

        Map<String, Object> body = Map.of("orderTables", orderTables);

        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(body)
                .when().log().all()
                .post("/api/table-groups")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(TableGroup.class);
    }

    private List<OrderTableResponse> getOrderTables() {
        return RestAssured.given().log().all()
                .when().log().all()
                .get("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getList(".", OrderTableResponse.class);
    }
}
