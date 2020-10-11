package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTable tableA;
    private OrderTable tableB;
    private OrderTable tableC;

    @BeforeEach
    void setUp() {
        super.setUp();
        tableA = createTable(0, true);
        tableB = createTable(0, true);
        tableC = createTable(0, true);
    }

    /**
     * Feature: 테이블 그룹을 관리한다. Scenario: 테이블들을 그룹짓고, 그룹 헤제한다.
     *
     * Given empty=true 인 테이블들이 존재한다.
     *
     * When empty=true 인 테이블들을 그룹짓는다. Then 테이블들이 그룹지어진다.
     *
     * When 그룹지은 테이블을 그룹 헤제한다. Then 테이블 그룹이 해제된다.
     */
    @Test
    @DisplayName("테이블 그룹 관리")
    void manageTableGroup() {
        // 테이블 그룹짓기
        TableGroup tableGroup = groupTables(Arrays.asList(tableA, tableB, tableC));

        assertThat(tableGroup.getId()).isNotNull();
        assertThatOrderTableBelongsTableGroup(tableA, tableGroup);
        assertThatOrderTableBelongsTableGroup(tableB, tableGroup);
        assertThatOrderTableBelongsTableGroup(tableC, tableGroup);

        // 테이블 그룹 헤제하기
        ungroup(tableGroup);
    }

    /**
     * Feature: 테이블 하나짜리 그룹 생성을 시도한다.
     *
     * Given empty=true 인 테이블들이 존재한다.
     *
     * When 테이블 하나짜리 그룹 생성을 시도한다.
     * Then
     */
    @Test
    @DisplayName("테이블 그룹 생성 - 테이블 한 개로 시도할 경우 예외처리")
    void createTableGroupWithOneTable() {
        assertThatFailToGroupTables(Collections.singletonList(tableA));
    }

    /**
     * Feature: 비어있지 않은 테이블들을 가지고 그룹 생성을 시도한다.
     *
     * Given 비어있는 테이블과 비어있지않은 테이블들이 존재한다.
     *
     * When 비어있지 않은 테이블이 포함된 테이블 그룹 생성을 시도한다.
     * Then 오류난다.    // Todo : 응답 리팩토링
     */
    @Test
    @DisplayName("테이블 그룹 생성 - 비어있지 않은 테이블을 포함하는 경우 예외처리")
    void createTableGroupWithTablesContainingNotEmpty() {
        changeEmptyToFalse(tableA);    // given

        assertThatFailToGroupTables(Arrays.asList(tableA, tableB, tableC));
    }

    /**
     * Feature: 이미 다른 그룹에 속해있는 테이블을 새로운 그룹에 포함시키려 한다.
     *
     * Given 이미 다른 그룹에 속한 테이블 A와 그렇지 않은 테이블들이 있다.
     *
     * When 테이블 A를 포함하여 그룹 생성하기를 시도한다.
     * Then 오류난다.    // Todo : 응답 리팩토링
     */
    @Test
    @DisplayName("테이블 그룹 생성 - 다른 그룹에 속한 테이블을 또 사용할 경우 예외처리")
    void createTableGroupWithTableAlreadyInAnotherGroup() {
        groupTables(Arrays.asList(tableA, tableB));    // given

        assertThatFailToGroupTables(Arrays.asList(tableC, tableA));
    }

    private TableGroup groupTables(List<OrderTable> orderTables) {
        Map<String, Object> body = new HashMap<>();

        List<Map> tablesForGroupingRequest = new ArrayList<>();

        for (OrderTable orderTable : orderTables) {
            Map<String, Object> tableForGroupingRequest = new HashMap<>();
            tableForGroupingRequest.put("id", orderTable.getId());

            tablesForGroupingRequest.add(tableForGroupingRequest);
        }
        body.put("orderTables", tablesForGroupingRequest);

        return
            given()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .post("/api/table-groups")
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(TableGroup.class);
    }

    private void ungroup(TableGroup tableGroup) {
        given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
        .when()
            .delete("/api/table-groups/" + tableGroup.getId())
        .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private void assertThatOrderTableBelongsTableGroup(OrderTable table, TableGroup tableGroup) {
        List<Long> tableIds = tableGroup.getOrderTables()
            .stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
        assertThat(tableIds).contains(table.getId());
    }

    private void assertThatFailToGroupTables(List<OrderTable> orderTables) {
        Map<String, Object> body = new HashMap<>();

        List<Map> tablesForGroupingRequest = new ArrayList<>();

        for (OrderTable orderTable : orderTables) {
            Map<String, Object> tableForGroupingRequest = new HashMap<>();
            tableForGroupingRequest.put("id", orderTable.getId());

            tablesForGroupingRequest.add(tableForGroupingRequest);
        }
        body.put("orderTables", tablesForGroupingRequest);

        given()
            .body(body)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
        .when()
            .post("/api/table-groups")
        .then()
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
