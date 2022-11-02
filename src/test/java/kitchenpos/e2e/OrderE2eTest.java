package kitchenpos.e2e;

import static java.time.LocalDateTime.now;
import static kitchenpos.support.fixture.OrderFixture.주문;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.presentation.dto.response.OrderLineItemResponse;
import kitchenpos.order.presentation.dto.response.OrderResponse;
import kitchenpos.support.KitchenPosE2eTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class OrderE2eTest extends KitchenPosE2eTest {


    @Test
    void create() {

        // given
        Long 주문테이블_ID = POST_요청(TABLE_URL, new OrderTable(0, false)).as(OrderTable.class).getId();
        LocalDateTime 주문일시 = now().minusMinutes(1);

        // when
        ExtractableResponse<Response> 응답 = 주문_생성(주문테이블_ID, 주문일시);
        OrderResponse 처리된_주문 = 응답.as(OrderResponse.class);
        OrderLineItemResponse 처리된_주문항목 = 처리된_주문.getOrderLineItems().get(0);

        // then
        assertAll(
                HTTP_STATUS_검증(HttpStatus.CREATED, 응답),
                NOT_NULL_검증(처리된_주문.getId()),
                NOT_NULL_검증(처리된_주문항목.getSeq()),
                단일_검증(처리된_주문.getOrderTableId(), 주문테이블_ID),
                단일_검증(처리된_주문.getOrderStatus(), OrderStatus.COOKING.name()),
                () -> assertThat(처리된_주문.getOrderedTime().isAfter(주문일시)).isTrue()
        );
    }

    @Test
    void list() {

        // given
        주문들_생성(3);

        // when
        ExtractableResponse<Response> 응답 = GET_요청(ORDER_URL);
        List<Order> 주문_리스트 = extractHttpBody(응답);

        // then
        assertAll(
                HTTP_STATUS_검증(HttpStatus.OK, 응답),
                () -> assertThat(주문_리스트).hasSize(3)
        );
    }

    @Test
    void changeOrderStatus() {

        // given
        OrderResponse 주문 = 주문_생성후_반환(OrderStatus.COOKING);

        // when
        ExtractableResponse<Response> 응답 =
                PUT_요청(ORDER_CHANGE_ORDER_STATUS_API, 주문.getId(), 주문(OrderStatus.MEAL));

        OrderResponse 상태바뀐_주문 = 응답.body().as(OrderResponse.class);

        assertAll(
                HTTP_STATUS_검증(HttpStatus.OK, 응답),
                단일_검증(상태바뀐_주문.getOrderStatus(), OrderStatus.MEAL.name())
        );
    }
}
