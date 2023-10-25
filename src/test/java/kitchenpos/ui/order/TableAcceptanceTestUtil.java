package kitchenpos.ui.order;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.application.order.request.TableCreateRequest;
import kitchenpos.application.order.request.TableUpdateRequest;
import kitchenpos.ui.ControllerAcceptanceTestHelper;
import kitchenpos.ui.order.response.TableResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

import java.util.List;

import static org.assertj.core.api.SoftAssertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public abstract class TableAcceptanceTestUtil extends ControllerAcceptanceTestHelper {

    protected TableCreateRequest 테이블생성_요청() {
        return new TableCreateRequest(0, true);
    }

    protected TableCreateRequest 채운_테이블생성_요청() {
        return new TableCreateRequest(0, false);
    }

    protected ExtractableResponse<Response> 테이블을_생성한다(TableCreateRequest 요청) {
        return RestAssured.given().body(요청)
                .contentType(ContentType.JSON)
                .when().post("/api/tables")
                .then()
                .extract();
    }

    protected void 테이블이_생성됨(TableCreateRequest 요청, ExtractableResponse<Response> 응답) {
        TableResponse response = 응답.as(TableResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(응답.statusCode()).isEqualTo(201);
            softly.assertThat(response.getTableGroupId()).isNull();
            softly.assertThat(response.getNumberOfGuests()).isEqualTo(요청.getNumberOfGuests());
            softly.assertThat(response.isEmpty()).isEqualTo(요청.isEmpty());
        });
    }

    protected ExtractableResponse<Response> 테이블_목록을_조회한다() {
        return RestAssured.given()
                .when().get("/api/tables")
                .then()
                .extract();
    }

    protected void 테이블_목록이_조회됨(ExtractableResponse<Response> 응답) {
        List<TableResponse> responses = 응답.jsonPath().getList(".", TableResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(응답.statusCode()).isEqualTo(200);
            softly.assertThat(responses).hasSize(1);
        });
    }

    protected Long 기존_테이블을_가져온다(ExtractableResponse<Response> 생성응답) {
        TableResponse response = 생성응답.as(TableResponse.class);
        return response.getId();
    }

    protected TableUpdateRequest 테이블_비움_요청() {
        return new TableUpdateRequest(0, true);
    }

    protected ExtractableResponse<Response> 테이블을_비운다(Long 기존테이블id, TableUpdateRequest 비움_요청) {
        return RestAssured.given().body(비움_요청)
                .contentType(ContentType.JSON)
                .when().put("/api/tables/{orderTableId}/empty", 기존테이블id)
                .then()
                .extract();
    }

    protected void 테이블이_비워짐(TableUpdateRequest 비움_요청, ExtractableResponse<Response> 비움_응답) {
        TableResponse response = 비움_응답.as(TableResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(비움_응답.statusCode()).isEqualTo(200);
            softly.assertThat(response.isEmpty()).isEqualTo(비움_요청.isEmpty());
        });
    }

    protected TableUpdateRequest 테이블_손님수_변경_요청() {
        return new TableUpdateRequest(10, false);
    }

    protected ExtractableResponse<Response> 테이블_손님수를_변경한다(Long 기존테이블id, TableUpdateRequest 손님수_변경_요청) {
        return RestAssured.given().body(손님수_변경_요청)
                .contentType(ContentType.JSON)
                .when().put("/api/tables/{orderTableId}/number-of-guests", 기존테이블id)
                .then()
                .extract();
    }

    protected void 테이블_손님수가_변경됨(TableUpdateRequest 손님수_변경_요청, ExtractableResponse<Response> 손님수_변경_응답) {
        TableResponse response = 손님수_변경_응답.as(TableResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(손님수_변경_응답.statusCode()).isEqualTo(200);
            softly.assertThat(response.getNumberOfGuests()).isEqualTo(손님수_변경_요청.getNumberOfGuests());
        });
    }
}
