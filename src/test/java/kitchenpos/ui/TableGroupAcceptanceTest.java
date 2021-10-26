package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("TableGroup 인수 테스트")
class TableGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("TableGroup 생성")
    @Test
    void create() {
        TableGroup tableGroup = tableGroup();
        TableGroup created = makeResponse("/api/table-groups", TestMethod.POST, tableGroup)
            .as(TableGroup.class);

        assertAll(
            () -> assertThat(created.getId()).isNotNull(),
            () -> assertThat(created.getOrderTables()).isNotEmpty()
        );
    }

    @DisplayName("TableGroup 생성 실패 - 테이블이 2개 미만이다.")
    @Test
    void create_fail_table_less_than_two() {
        TableGroup tableGroup = tableGroup();
        tableGroup.setOrderTables(new ArrayList<>());
        ExtractableResponse<Response> response = makeResponse("/api/table-groups", TestMethod.POST,
            tableGroup);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("TableGroup 생성 실패 - 존재하지 않는 테이블이 있다.")
    @Test
    void create_fail_table_non_exist() {
        TableGroup tableGroup = tableGroup();
        tableGroup.setOrderTables(Arrays.asList(new OrderTable(), new OrderTable()));

        ExtractableResponse<Response> response = makeResponse("/api/table-groups", TestMethod.POST,
            tableGroup);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("TableGroup 생성 실패 - 비지 않은 테이블이 있다.")
    @Test
    void create_fail_table_not_empty() {
        TableGroup tableGroup = tableGroup();
        OrderTable orderTable1 = new OrderTable(1, false);
        OrderTable orderTable2 = new OrderTable(1, false);
        makeResponse("/api/tables", TestMethod.POST, orderTable1);
        makeResponse("/api/tables", TestMethod.POST, orderTable2);
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        ExtractableResponse<Response> response = makeResponse("/api/table-groups", TestMethod.POST,
            tableGroup);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("TableGroup 해제")
    @Test
    void ungroup() {
        TableGroup tableGroup = tableGroup();
        TableGroup created = makeResponse("/api/table-groups", TestMethod.POST, tableGroup)
            .as(TableGroup.class);

        makeResponse("/api/table-groups/" + created.getId(), TestMethod.DELETE);

        List<OrderTable> ungroupedTables = makeResponse("/api/tables", TestMethod.GET).jsonPath()
            .getList(".", OrderTable.class);
        boolean actual = ungroupedTables.stream()
            .noneMatch(table -> created.getId().equals(table.getTableGroupId()));
        assertThat(actual).isTrue();
    }

    @DisplayName("TableGroup 해제 실패 - 테이블 상태가 식사 완료가 아니다.")
    @Test
    void ungroup_fail_unable_order_status() {
        TableGroup tableGroup = tableGroup();
        TableGroup createdTableGroup = makeResponse("/api/table-groups", TestMethod.POST, tableGroup)
            .as(TableGroup.class);
        OrderTable orderTable = createdTableGroup.getOrderTables().get(0);
        Order order = order();
        order.setOrderTableId(orderTable.getId());
        makeResponse("/api/orders", TestMethod.POST, order).as(Order.class);

        ExtractableResponse<Response> response = makeResponse(
            "/api/table-groups/" + createdTableGroup.getId(), TestMethod.DELETE);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}