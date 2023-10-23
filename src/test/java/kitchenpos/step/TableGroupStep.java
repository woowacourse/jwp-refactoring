package kitchenpos.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.request.TableGroupCreateRequest;

import static io.restassured.http.ContentType.JSON;

public class TableGroupStep {

    public static Long 테이블_그룹_생성_요청하고_아이디_반환(final TableGroup tableGroup) {
        final ExtractableResponse<Response> response = 테이블_그룹_생성_요청(tableGroup);
        return response.jsonPath().getLong("id");
    }

    public static ExtractableResponse<Response> 테이블_그룹_생성_요청(final TableGroup tableGroup) {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)
                .body(tableGroup)

                .when()
                .post("/api/table-groups")

                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블_그룹_삭제_요청(final long tableGroupId) {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)

                .when()
                .delete("/api/table-groups/" + tableGroupId)

                .then()
                .log().all()
                .extract();
    }

    public static Long 테이블_그룹_생성_요청하고_아이디_반환(final TableGroupCreateRequest request) {
        final ExtractableResponse<Response> response = 테이블_그룹_생성_요청(request);
        return response.jsonPath().getLong("id");
    }

    public static ExtractableResponse<Response> 테이블_그룹_생성_요청(final TableGroupCreateRequest reuest) {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)
                .body(reuest)

                .when()
                .post("/api/table-groups")

                .then()
                .log().all()
                .extract();
    }
}
