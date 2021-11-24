package kitchenpos.ui;

import io.restassured.response.Response;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasItems;

class TableGroupRestControllerTest extends AcceptanceTest {
    @Test
    @DisplayName("테이블 그룹을 생성한다.")
    void create() {
        // given
        Long table1Id = createTable();
        Long table2Id = createTable();

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(table1Id, table2Id));

        // when
        Response response = post("/api/table-groups", tableGroupRequest);

        // then
        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .body("orderTables.id", hasItems(table1Id.intValue(), table2Id.intValue()));
    }

    @Test
    @DisplayName("테이블 그룹을 삭제한다.")
    void delete() {
        // given
        Long table1Id = createTable();
        Long table2Id = createTable();

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(table1Id, table2Id));
        TableGroupResponse tableGroupResponse = post("/api/table-groups", tableGroupRequest).then().extract().as(TableGroupResponse.class);

        // when
        Response response = delete("/api/table-groups/" + tableGroupResponse.getId());

        // then
        response.then().assertThat().statusCode(HttpStatus.NO_CONTENT.value());
    }

    private Long createTable() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(4, true);
        return post("/api/tables", orderTableRequest)
                .then()
                .extract()
                .as(OrderTableResponse.class)
                .getId();
    }
}
