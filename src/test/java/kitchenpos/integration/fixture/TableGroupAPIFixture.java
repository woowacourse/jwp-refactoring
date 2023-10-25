package kitchenpos.integration.fixture;

import static io.restassured.RestAssured.given;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.application.dto.request.TableGroupCreateRequest;
import kitchenpos.application.dto.response.TableGroupResponse;
import org.springframework.http.MediaType;

public class TableGroupAPIFixture {

    public static ExtractableResponse<Response> createTableGroup(final TableGroupCreateRequest request) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .body(request)
                .post("/api/table-groups")
                .then()
                .log().all()
                .extract();
    }

    public static TableGroupResponse createTableGroupAndReturnResponse(final TableGroupCreateRequest request) {
        return createTableGroup(request).as(TableGroupResponse.class);
    }

    public static void ungroup(final Long tableGroupId) {
        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/table-groups/{tableGroupId}", tableGroupId)
                .then()
                .log().all()
                .extract();
    }
}
