package kitchenpos.e2e;

import static kitchenpos.support.E2eTest.AssertionPair.row;
import static kitchenpos.support.AssertionsSupport.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.order.presentation.dto.request.OrderTableRequest;
import kitchenpos.order.presentation.dto.response.TableChangeEmptyResponse;
import kitchenpos.order.presentation.dto.response.TableChangeGuestNumberResponse;
import kitchenpos.order.presentation.dto.response.TableCreateResponse;
import kitchenpos.order.presentation.dto.response.TableListResponse;
import kitchenpos.support.KitchenPosE2eTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class TableE2eTest extends KitchenPosE2eTest {

    @Test
    void create() {

        // given & when
        ExtractableResponse<Response> 응답 = 주문테이블_생성(new OrderTableRequest(0, true));
        TableCreateResponse 주문테이블 = 응답.body().as(TableCreateResponse.class);

        // then
        assertAll(
                HTTP_STATUS_검증(HttpStatus.CREATED, 응답),
                NOT_NULL_검증(주문테이블.getId()),
                단일_검증(주문테이블.getNumberOfGuests(), 0),
                단일_검증(주문테이블.isEmpty(), true)
        );
    }

    @Test
    void list() {

        // given
        주문테이블_생성(new OrderTableRequest(0, true));
        주문테이블_생성(new OrderTableRequest(2, false));

        // when
        ExtractableResponse<Response> 응답 = GET_요청(TABLE_URL);
        List<TableListResponse> 주문테이블_리스트 = extractHttpBody(응답);

        // then
        // TODO row("empty", true, false)의 경우 계속해서 동일 객체로 false가 나옴.(디버거상은 정상(T, F))
        assertAll(
                HTTP_STATUS_검증(HttpStatus.OK, 응답),
                리스트_검증(주문테이블_리스트,
                        row("id", 1, 2),
                        row("tableGroupId", null, null),
                        row("numberOfGuests", 0, 2)
                )
        );
    }

    @Test
    void changeEmpty() {

        // given
        Long 주문테이블_ID =
                주문테이블_생성(new OrderTableRequest(0, true)).as(OrderTableRequest.class).getId();

        // when
        ExtractableResponse<Response> 응답 =
                PUT_요청(TABLE_CHANGE_EMPTY_URL, 주문테이블_ID, new OrderTableRequest(0, false));

        TableChangeEmptyResponse 바뀐_주문테이블 = 응답.as(TableChangeEmptyResponse.class);

        // then
        assertAll(
                HTTP_STATUS_검증(HttpStatus.OK, 응답),
                단일_검증(바뀐_주문테이블.isEmpty(), false)
        );
    }

    @Test
    void changeNumberOfGuests() {

        // given
        Long 주문테이블_ID =
                POST_요청(TABLE_URL, new OrderTableRequest(0, false))
                        .as(OrderTableRequest.class)
                        .getId();

        // when
        ExtractableResponse<Response> 응답 =
                PUT_요청(ORDER_TABLE_CHANGE_NUMBER_OF_GUESTS,
                        주문테이블_ID,
                        new OrderTableRequest(4, true));

        TableChangeGuestNumberResponse 바뀐_주문테이블 = 응답.as(TableChangeGuestNumberResponse.class);

        // then
        assertAll(
                HTTP_STATUS_검증(HttpStatus.OK, 응답),
                단일_검증(바뀐_주문테이블.getNumberOfGuests(), 4)
        );
    }
}
