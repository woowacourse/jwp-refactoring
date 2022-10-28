package kitchenpos.e2e;

import static java.lang.String.format;
import static kitchenpos.e2e.E2eTest.AssertionPair.row;
import static kitchenpos.support.AssertionsSupport.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class TableE2eTest extends E2eTest {

    @Test
    void create() {

        // given & when
        final ExtractableResponse<Response> 응답 = POST_요청(ORDER_TABLE_URL, new OrderTable(0, true));
        final OrderTable 주문테이블 = 응답.body().as(OrderTable.class);

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
        POST_요청(ORDER_TABLE_URL, new OrderTable(0, true));
        POST_요청(ORDER_TABLE_URL, new OrderTable(2, false));

        // when
        final ExtractableResponse<Response> 응답 = GET_요청(ORDER_TABLE_URL);
        final List<OrderTable> 주문테이블_리스트 = extractHttpBody(응답);

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
        final Long 주문테이블_ID =
                POST_요청(ORDER_TABLE_URL, new OrderTable(0, true))
                        .as(OrderTable.class)
                        .getId();

        // when
        final ExtractableResponse<Response> 응답 = PUT_요청(
                format("/api/tables/%s/empty", 주문테이블_ID.toString()), new OrderTable(0, false));

        final OrderTable 바뀐_주문테이블 = 응답.as(OrderTable.class);

        // then
        assertAll(
                단일_검증(바뀐_주문테이블.getId(), 주문테이블_ID),
                단일_검증(바뀐_주문테이블.isEmpty(), false),
                단일_검증(바뀐_주문테이블.getNumberOfGuests(), 0),
                단일_검증(바뀐_주문테이블.getTableGroupId(), null)
        );
    }

    @Test
    void changeNumberOfGuests() {

        // given
        final Long 주문테이블_ID =
                POST_요청(ORDER_TABLE_URL, new OrderTable(0, true))
                        .as(OrderTable.class)
                        .getId();


        // when
        final ExtractableResponse<Response> 응답 = PUT_요청(
                format("/api/tables/{orderTableId}/number-of-guests", 주문테이블_ID.toString())
                , new OrderTable(4, true));

        final OrderTable 바뀐_주문테이블 = 응답.as(OrderTable.class);

        // then
        assertAll(
                단일_검증(바뀐_주문테이블.getId(), 주문테이블_ID),
                단일_검증(바뀐_주문테이블.isEmpty(), false),
                단일_검증(바뀐_주문테이블.getNumberOfGuests(), 4),
                단일_검증(바뀐_주문테이블.getTableGroupId(), null)
        );
    }
}
