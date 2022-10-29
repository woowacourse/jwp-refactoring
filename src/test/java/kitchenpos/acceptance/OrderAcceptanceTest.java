package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuAcceptanceTest.메뉴를_생성한다;
import static kitchenpos.acceptance.MenuGroupAcceptanceTest.메뉴_그룹을_생성한다;
import static kitchenpos.acceptance.OrderTableAcceptanceTest.테이블을_생성한다;
import static kitchenpos.acceptance.ProductAcceptanceTest.상품을_생성한다;
import static kitchenpos.acceptance.support.RequestUtil.get;
import static kitchenpos.acceptance.support.RequestUtil.post;
import static kitchenpos.acceptance.support.RequestUtil.put;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;

public class OrderAcceptanceTest extends AcceptanceTest {

    static ExtractableResponse<Response> 주문을_생성한다(final Long orderTableId, List<Map<String, Long>> orderLineItems) {
        Map<Object, Object> body = new HashMap<>();
        body.put("orderTableId", orderTableId);
        body.put("orderLineItems", orderLineItems);

        return post("/api/orders", body);
    }

    static ExtractableResponse<Response> 모든_주문을_조회한다() {
        return get("/api/orders");
    }

    static ExtractableResponse<Response> 주문의_주문_상태를_변경한다(final Long orderTableId, final OrderStatus orderStatus) {
        return put("/api/orders/" + orderTableId + "/order-status", Map.of(
                "orderStatus", orderStatus.name()
        ));
    }

    @DisplayName("주문을 관리한다.")
    @TestFactory
    Stream<DynamicTest> mangeOrder() {
        // given
        long 상품1_ID = 상품을_생성한다("후라이드", 15_000).jsonPath().getLong("id");
        long 상품2_ID = 상품을_생성한다("후라이드", 15_000).jsonPath().getLong("id");

        long 메뉴_그룹_ID = 메뉴_그룹을_생성한다("치킨").jsonPath().getLong("id");
        long 메뉴1_ID = 메뉴를_생성한다("후라이드 세트", 10_000, 메뉴_그룹_ID, List.of(
                Map.of("productId", 상품1_ID, "quantity", 1L)
        )).jsonPath().getLong("id");
        long 메뉴2_ID = 메뉴를_생성한다("양념 세트", 10_000, 메뉴_그룹_ID, List.of(
                Map.of("productId", 상품2_ID, "quantity", 1L)
        )).jsonPath().getLong("id");

        long 테이블_ID = 테이블을_생성한다(10, false).jsonPath().getLong("id");

        return Stream.of(
                dynamicTest("주문을 생성한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 주문을_생성한다(테이블_ID, List.of(
                            Map.of("menuId", 메뉴1_ID, "quantity", 1L),
                            Map.of("menuId", 메뉴2_ID, "quantity", 1L)
                    ));

                    // then
                    상태코드를_검증한다(response, HttpStatus.CREATED);
                    필드가_Null이_아닌지_검증한다(response, "id");
                    리스트_길이를_검증한다(response, "orderLineItems", 2);
                }),
                dynamicTest("모든 주문을 조회한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 모든_주문을_조회한다();

                    // then
                    상태코드를_검증한다(response, HttpStatus.OK);
                    리스트_길이를_검증한다(response, ".", 1);
                }),
                dynamicTest("주문의 주문 상태를 변경한다.", () -> {
                    // when
                    long 주문_ID = 모든_주문을_조회한다().jsonPath().getLong("[0].id");
                    ExtractableResponse<Response> response = 주문의_주문_상태를_변경한다(주문_ID, OrderStatus.MEAL);

                    // then
                    상태코드를_검증한다(response, HttpStatus.OK);
                    문자열_필드값을_검증한다(response, "orderStatus", OrderStatus.MEAL.name());
                })
        );
    }
}
