package kitchenpos.acceptance.table;

import static kitchenpos.acceptance.AcceptanceSteps.given;
import static kitchenpos.acceptance.AcceptanceSteps.생성된_ID를_추출한다;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.application.dto.OrderTableCreateRequest;

@SuppressWarnings("NonAsciiCharacters")
public class TableAcceptanceSteps {

    public static final boolean 비어있음 = true;
    public static final boolean 비어있지_않음 = false;

    public static Long 테이블_등록후_생성된_ID를_가져온다(int 손님_수, boolean 비어있음_여부) {
        return 생성된_ID를_추출한다(테이블_등록_요청을_보낸다(손님_수, 비어있음_여부));
    }

    public static ExtractableResponse<Response> 테이블_등록_요청을_보낸다(int 손님_수, boolean 비어있음_여부) {
        OrderTableCreateRequest request = new OrderTableCreateRequest(손님_수, 비어있음_여부);
        return given()
                .body(request)
                .post("/api/tables")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블의_비어있음_상태_변경_요청을_보낸다(Long 테이블_ID, boolean 비어있음_여부) {
        OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(비어있음_여부);
        return given()
                .body(request)
                .put("/api/tables/{tableId}/empty", 테이블_ID)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블_손님_수_변경_요청을_보낸다(Long 테이블_ID, int 손님_수) {
        OrderTableChangeNumberOfGuestsRequest request = new OrderTableChangeNumberOfGuestsRequest(손님_수);
        return given()
                .body(request)
                .put("/api/tables/{orderTableId}/number-of-guests", 테이블_ID)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블_조회_요청을_보낸다() {
        return given()
                .get("/api/tables")
                .then()
                .log().headers()
                .extract();
    }
}
