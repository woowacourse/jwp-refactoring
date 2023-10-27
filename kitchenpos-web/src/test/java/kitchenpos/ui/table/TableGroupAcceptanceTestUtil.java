package kitchenpos.ui.table;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.order.request.TableCreateRequest;
import kitchenpos.table.TableGroupService;
import kitchenpos.table.request.OrderTableIdRequest;
import kitchenpos.table.request.TableGroupCreateRequest;
import kitchenpos.ui.ControllerAcceptanceTestHelper;
import kitchenpos.ui.order.response.TableResponse;
import kitchenpos.ui.table.response.TableGroupResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class TableGroupAcceptanceTestUtil extends ControllerAcceptanceTestHelper {

    @Autowired
    private TableGroupService tableGroupService;

    protected Long 테이블_생성_id() {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(0, true);

        ExtractableResponse<Response> response = RestAssured.given().body(tableCreateRequest)
                .contentType(ContentType.JSON)
                .when().post("/api/tables")
                .then()
                .extract();

        return response.as(TableResponse.class).getId();
    }

    protected List<OrderTableIdRequest> 그룹화할_테이블id_요청(Long... ids) {
        return List.of(ids).stream()
                .map(OrderTableIdRequest::new)
                .toList();
    }

    protected TableGroupCreateRequest 그룹화_요청(List<OrderTableIdRequest> 그룹화할_테이블_id_요청) {
        return new TableGroupCreateRequest(그룹화할_테이블_id_요청);
    }

    protected ExtractableResponse<Response> 테이블을_그룹화한다(TableGroupCreateRequest 요청) {
        return RestAssured.given().body(요청)
                .contentType(ContentType.JSON)
                .when().post("/api/table-groups")
                .then()
                .extract();
    }

    protected void 테이블이_그룹화됨(TableGroupCreateRequest 요청, ExtractableResponse<Response> 응답) {
        TableGroupResponse response = 응답.as(TableGroupResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(응답.statusCode()).isEqualTo(201);
            softly.assertThat(response.getId()).isNotNull();
            softly.assertThat(response.getOrderTables()).isNotNull();
            softly.assertThat(response.getOrderTables().size()).isEqualTo(요청.getOrderTableIds().size());
        });
    }

    protected Long 그룹테이블_id_조회(ExtractableResponse<Response> 응답) {
        TableGroupResponse response = 응답.as(TableGroupResponse.class);
        return response.getId();
    }

    protected ExtractableResponse<Response> 테이블을_비그룹화한다(Long 그룹테이블_id) {
        return RestAssured.given()
                .when().delete("/api/table-groups/{tableGroupId}", 그룹테이블_id)
                .then()
                .extract();
    }

    protected void 테이블이_비그룹화됨(ExtractableResponse<Response> 응답) {
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(응답.statusCode()).isEqualTo(204);
        });
    }
}
