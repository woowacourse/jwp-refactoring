package kitchenpos.acceptance;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.dto.request.CreateMenuRequest;
import kitchenpos.dto.request.CreateOrderRequest;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.support.TestMenuFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
class OrderTest extends AcceptanceTest {

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
        CreateMenuRequest menuRequest = MenuFixture.REQUEST.후라이드_치킨_16000원_1마리_등록_요청();
        Long menuId = MenuFixture.메뉴_생성(menuRequest);
        CreateOrderRequest request = OrderFixture.REQUEST.주문_생성_요청(menuId);
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
                    softly.assertThat(response.body().jsonPath().getString("[0].orderLineItems[0].name")).isEqualTo(menuRequest.getName());
                    softly.assertThat(new BigDecimal(response.body().jsonPath().getString("[0].orderLineItems[0].price"))).isEqualByComparingTo(menuRequest.getPrice());
                }
        );
    }

    @Test
    void 메뉴_정보가_변경되어도_주문정보는_동일하다() {
        // given
        CreateMenuRequest menuRequest = MenuFixture.REQUEST.후라이드_치킨_16000원_1마리_등록_요청();
        Long menuId = MenuFixture.메뉴_생성(menuRequest);
        CreateOrderRequest request = OrderFixture.REQUEST.주문_생성_요청(menuId);
        Long orderId = OrderFixture.주문_생성(request);

        // when
        CreateMenuRequest updateRequest = MenuFixture.REQUEST.양념치킨_17000원_1마리_등록_요청();
        TestMenuFixture.메뉴_수정(menuId, updateRequest);

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
                    softly.assertThat(response.body().jsonPath().getString("[0].orderLineItems[0].name")).isEqualTo(menuRequest.getName());
                }
        );
    }
}
