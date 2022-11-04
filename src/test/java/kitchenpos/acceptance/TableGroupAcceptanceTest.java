package kitchenpos.acceptance;

import static kitchenpos.fixture.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.table.ui.dto.tablegroup.TableGroupCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class TableGroupAcceptanceTest extends AcceptanceTest {

    private static final String API = "/api/table-groups";

    @DisplayName("테이블 그룹을 생성할 수 있다.")
    @Test
    void createTableGroup() {
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(List.of(ORDER_TABLE_1.getId(), ORDER_TABLE_2.getId()));

        ExtractableResponse<Response> response = HttpMethodFixture.httpPost(tableGroupCreateRequest, API);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("테이블 그룹을 해제할 수 있다.")
    @Test
    void ungroup() {
        ExtractableResponse<Response> createTableGroupResponse = HttpMethodFixture.httpPost(new TableGroupCreateRequest(List.of(ORDER_TABLE_1.getId(), ORDER_TABLE_2.getId())), API);

        Long tableGroupId = createTableGroupResponse.body().jsonPath().getObject("id", Long.class);

        String api = API + "/" + tableGroupId;
        ExtractableResponse<Response> response = HttpMethodFixture.httpDelete(api);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
