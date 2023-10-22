package kitchenpos.acceptance.table;

import static kitchenpos.acceptance.AcceptanceSteps.given;
import static kitchenpos.acceptance.AcceptanceSteps.생성된_ID를_추출한다;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import kitchenpos.table.application.dto.TableGroupCreateRequest;
import kitchenpos.table.application.dto.TableGroupCreateRequest.TableInfo;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupAcceptanceSteps {

    public static Long 테이블_그룹_등록후_생성된_ID를_가져온다(Long... 테이블_ID들) {
        return 생성된_ID를_추출한다(테이블_그룹_등록_요청을_보낸다(테이블_ID들));
    }

    public static ExtractableResponse<Response> 테이블_그룹_등록_요청을_보낸다(Long... 테이블_ID들) {
        TableGroupCreateRequest request = new TableGroupCreateRequest(
                Arrays.stream(테이블_ID들)
                        .map(TableInfo::new)
                        .toList()
        );
        return given()
                .body(request)
                .post("/api/table-groups")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블_그룹_제거_요청을_보낸다(Long 테이블_그룹_ID) {
        return given()
                .delete("/api/table-groups/{tableGroupId}", 테이블_그룹_ID)
                .then()
                .log().all()
                .extract();
    }

}
