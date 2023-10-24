package kitchenpos.acceptance;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.Acceptance;
import kitchenpos.dto.request.CreateOrderRequest;
import kitchenpos.fixture.OrderFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
class OrderTest extends Acceptance {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 주문을_등록한다() {
        // given
        CreateOrderRequest request = OrderFixture.REQUEST.주문_생성_요청();

        // when & then
        RestAssured.given()
                .body(request)
                .contentType(ContentType.JSON)
                .when().post("/api/orders")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/api/orders/1");
    }

    @Test
    void 주문을_조회한다() {
        // given
        CreateOrderRequest request = OrderFixture.REQUEST.주문_생성_요청();
        Long orderId = OrderFixture.주문_생성(request);

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .when().get("/api/orders")
                .then().log().all()
                .statusCode(200)
                .extract();

        // then
        assertSoftly(softly -> {
                    softly.assertThat(response.body().jsonPath().getList(".")).hasSize(1);
                    softly.assertThat(response.body().jsonPath().getLong("[0].id")).isEqualTo(orderId);
                    softly.assertThat(response.body().jsonPath().getLong("[0].orderTableId")).isEqualTo(request.getOrderTableId());
                    softly.assertThat(response.body().jsonPath().getList("[0].orderLineItems")).hasSize(1);
                    softly.assertThat(response.body().jsonPath().getLong("[0].orderLineItems[0].menuId")).isEqualTo(request.getOrderLineItems().get(0).getMenuId());
                    softly.assertThat(response.body().jsonPath().getLong("[0].orderLineItems[0].quantity")).isEqualTo(request.getOrderLineItems().get(0).getQuantity());
                }
        );
    }
}
