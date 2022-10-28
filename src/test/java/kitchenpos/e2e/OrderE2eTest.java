package kitchenpos.e2e;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static kitchenpos.support.OrderFixture.주문;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class OrderE2eTest extends E2eTest {

    @Test
    void create() {

        // given
        final Long 주문테이블_ID = POST_요청(TABLE_URL, new OrderTable(0, false)).as(OrderTable.class).getId();
        final LocalDateTime 주문일시 = now().minusMinutes(1);

        // when
        final Order 처리된_주문 = 주문_생성(주문테이블_ID, 주문일시);
        final OrderLineItem 처리된_주문항목 = 처리된_주문.getOrderLineItems().get(0);

        // then
        assertAll(
                NOT_NULL_검증(처리된_주문.getId()),
                NOT_NULL_검증(처리된_주문항목.getSeq()),
                단일_검증(처리된_주문.getOrderTableId(), 주문테이블_ID),
                단일_검증(처리된_주문항목.getOrderId(), 처리된_주문.getId()),
                단일_검증(처리된_주문.getOrderStatus(), OrderStatus.COOKING.name()),
                () -> assertThat(처리된_주문.getOrderedTime().isAfter(주문일시)).isTrue()
        );
    }

    @Test
    void list() {

        // given
        final Order 주문_1 = 주문_생성();
        final Order 주문_2 = 주문_생성();
        final Order 주문_3 = 주문_생성();

        // when
        final ExtractableResponse<Response> 응답 = GET_요청(ORDER_URL);
        final List<Order> 주문_리스트 = extractHttpBody(응답);

        // then
        assertThat(주문_리스트).hasSize(3);
    }

    @Test
    void changeOrderStatus() {

        // given
        final Order 주문 = 주문_생성(OrderStatus.COOKING);

        // when
        final ExtractableResponse<Response> 응답 =
                PUT_요청("/api/orders/{orderId}/order-status", 주문.getId(), 주문(OrderStatus.MEAL));

        final Order 상태바뀐_주문 = 응답.body().as(Order.class);

        assertAll(
                HTTP_STATUS_검증(HttpStatus.OK, 응답),
                단일_검증(상태바뀐_주문.getOrderStatus(), OrderStatus.MEAL.name())
        );
    }
}
