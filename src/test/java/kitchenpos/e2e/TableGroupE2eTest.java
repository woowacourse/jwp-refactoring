package kitchenpos.e2e;

import static kitchenpos.e2e.E2eTest.AssertionPair.row;
import static kitchenpos.support.AssertionsSupport.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.temporal.ChronoUnit;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.TableGroupResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class TableGroupE2eTest extends KitchenPosE2eTest {

    @Test
    void create() {

        // given
        List<OrderTableRequest> 주문테이블들 = 주문테이블들_생성(2);

        // when
        ExtractableResponse<Response> 응답 = POST_요청(TABLE_GROUP_URL, new TableGroupRequest(null, 주문테이블들));
        TableGroupResponse 저장된_테이블그룹 = 응답.body().as(TableGroupResponse.class);

        // then
        assertAll(
                HTTP_STATUS_검증(HttpStatus.CREATED, 응답),
                NOT_NULL_검증(저장된_테이블그룹.getId()),
                시간_근사_검증(저장된_테이블그룹.getCreatedDate(), 3, ChronoUnit.MINUTES)
        );
    }


    @Test
    void ungroup() {

        // given
        List<OrderTableRequest> 주문테이블들 = 주문테이블들_생성(2, new OrderTableRequest(0, true));
        TableGroupResponse 저장된_테이블그룹 = 테이블그룹_생성(new TableGroupRequest(null, 주문테이블들));

        // when
        ExtractableResponse<Response> 응답 =
                DELETE_요청(TABLE_GROUP_DELETE_URL, 저장된_테이블그룹.getId(), new TableGroupRequest(null, 주문테이블들));
        List<OrderTable> 갱신된_주문테이블_리스트 = extractHttpBody(GET_요청(TABLE_URL));

        // then
        assertAll(
                HTTP_STATUS_검증(HttpStatus.NO_CONTENT, 응답),
                리스트_검증(갱신된_주문테이블_리스트,
                        row("tableGroupId", null, null),
                        row("empty", false, false)
                )
        );
    }
}
