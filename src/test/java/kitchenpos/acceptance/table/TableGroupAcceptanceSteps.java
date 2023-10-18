package kitchenpos.acceptance.table;

import static kitchenpos.acceptance.AcceptanceSteps.given;
import static kitchenpos.acceptance.AcceptanceSteps.생성된_ID를_추출한다;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupAcceptanceSteps {

    public static Long 테이블_그룹_등록후_생성된_ID를_가져온다(Long... 테이블_ID들) {
        return 생성된_ID를_추출한다(테이블_그룹_등록_요청을_보낸다(테이블_ID들));
    }

    public static ExtractableResponse<Response> 테이블_그룹_등록_요청을_보낸다(Long... 테이블_ID들) {
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> list = Arrays.stream(테이블_ID들)
                .map(it -> {
                    OrderTable orderTable = new OrderTable();
                    orderTable.setId(it);
                    return orderTable;
                })
                .toList();
        tableGroup.setOrderTables(list);
        return given()
                .body(tableGroup)
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
