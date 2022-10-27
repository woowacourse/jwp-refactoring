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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class OrderTableAcceptanceTest extends AcceptanceTest {

    @DisplayName("테이블 목록을 조회한다.")
    @Test
    void findOrderTables() {
        // act
        List<OrderTable> orderTables = getOrderTables();

        // assert
        assertThat(orderTables)
                .extracting(
                        OrderTable::getId, OrderTable::getNumberOfGuests,
                        OrderTable::isEmpty, OrderTable::getTableGroupId
                )
                .hasSize(8)
                .containsExactlyInAnyOrder(
                        tuple(1L, 0, true, null),
                        tuple(2L, 0, true, null),
                        tuple(3L, 0, true, null),
                        tuple(4L, 0, true, null),
                        tuple(5L, 0, true, null),
                        tuple(6L, 0, true, null),
                        tuple(7L, 0, true, null),
                        tuple(8L, 0, true, null)
                );
    }

    @DisplayName("빈 테이블을 주문 테이블로 변경한다.")
    @Test
    void changeOrderTableToEmptyFalse() {
        // act
        OrderTable orderTable = changeTableEmptyStatus(1L, false);

        // assert
        assertThat(orderTable.getId()).isEqualTo(1L);
        assertThat(orderTable.isEmpty()).isFalse();

        List<OrderTable> orderTables = getOrderTables();
        assertThat(orderTables)
                .extracting(
                        OrderTable::getId, OrderTable::getNumberOfGuests,
                        OrderTable::isEmpty, OrderTable::getTableGroupId
                )
                .containsExactlyInAnyOrder(
                        tuple(1L, 0, false, null),
                        tuple(2L, 0, true, null),
                        tuple(3L, 0, true, null),
                        tuple(4L, 0, true, null),
                        tuple(5L, 0, true, null),
                        tuple(6L, 0, true, null),
                        tuple(7L, 0, true, null),
                        tuple(8L, 0, true, null)
                );
    }

    @DisplayName("테이블의 방문 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // arrange
        changeTableEmptyStatus(1L, false);

        // act
        OrderTable orderTable = changeTableNumberOfGuests(1L, 10);

        // assert
        assertThat(orderTable.getId()).isEqualTo(1L);
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);

        List<OrderTable> orderTables = getOrderTables();
        assertThat(orderTables)
                .extracting(
                        OrderTable::getId, OrderTable::getNumberOfGuests,
                        OrderTable::isEmpty, OrderTable::getTableGroupId
                )
                .containsExactlyInAnyOrder(
                        tuple(1L, 10, false, null),
                        tuple(2L, 0, true, null),
                        tuple(3L, 0, true, null),
                        tuple(4L, 0, true, null),
                        tuple(5L, 0, true, null),
                        tuple(6L, 0, true, null),
                        tuple(7L, 0, true, null),
                        tuple(8L, 0, true, null)
                );
    }

    @DisplayName("테이블을 생성한다.")
    @Test
    void createTable() {
        OrderTable orderTable = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(Map.of("numberOfGuests", 10, "empty", false))
                .when().log().all()
                .post("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(OrderTable.class);

        // assert
        assertThat(orderTable.getId()).isNotNull();
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);
        assertThat(orderTable.isEmpty()).isFalse();
        assertThat(orderTable.getTableGroupId()).isNull();

        List<OrderTable> orderTables = getOrderTables();
        assertThat(orderTables)
                .extracting(
                        OrderTable::getId, OrderTable::getNumberOfGuests,
                        OrderTable::isEmpty, OrderTable::getTableGroupId
                )
                .hasSize(9)
                .contains(tuple(orderTable.getId(), 10, false, null));
    }

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

        List<OrderTable> orderTables = getOrderTables();
        assertThat(orderTables)
                .extracting(
                        OrderTable::getId, OrderTable::getNumberOfGuests,
                        OrderTable::isEmpty, OrderTable::getTableGroupId
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
        List<OrderTable> orderTables = getOrderTables();
        assertThat(orderTables)
                .extracting(
                        OrderTable::getId, OrderTable::getNumberOfGuests,
                        OrderTable::isEmpty, OrderTable::getTableGroupId
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

    private List<OrderTable> getOrderTables() {
        return RestAssured.given().log().all()
                .when().log().all()
                .get("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getList(".", OrderTable.class);
    }

    private OrderTable changeTableEmptyStatus(long tableId, boolean tableStatus) {
        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .pathParam("table-id", tableId)
                .body(Map.of("empty", tableStatus))
                .when().log().all()
                .put("/api/tables/{table-id}/empty")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(OrderTable.class);
    }

    private OrderTable changeTableNumberOfGuests(long tableId, int numberOfGuests) {
        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .pathParam("table-id", tableId)
                .body(Map.of("numberOfGuests", numberOfGuests))
                .when().log().all()
                .put("/api/tables/{table-id}/number-of-guests")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(OrderTable.class);
    }
}
