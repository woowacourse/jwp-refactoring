package kitchenpos.fixture;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.dto.request.CreateMenuGroupRequest;
import kitchenpos.dto.request.CreateMenuRequest;

import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class MenuFixture {

    private MenuFixture() {
    }

    public static class REQUEST {

        public static CreateMenuRequest 후라이드_치킨_16000원_1마리_등록_요청() {
            Long productId = ProductFixture.상품_생성(ProductFixture.REQUEST.후라이드_치킨_16000원());
            Long menuGroupId = MenuGroupFixture.메뉴_그룹_생성(CreateMenuGroupRequest.builder().name("치킨류").build());
            return CreateMenuRequest.builder()
                    .name("후라이드치킨")
                    .menuGroupId(menuGroupId)
                    .price(BigDecimal.valueOf(16000L))
                    .menuProducts(List.of(MenuProductFixture.REQUEST.상품_N_M개_요청(productId, 1)))
                    .build();
        }

        public static CreateMenuRequest 양념치킨_17000원_1마리_등록_요청() {
            Long productId = ProductFixture.상품_생성(ProductFixture.REQUEST.양념_치킨_17000원());
            Long menuGroupId = MenuGroupFixture.메뉴_그룹_생성(CreateMenuGroupRequest.builder().name("양념치킨류").build());
            return CreateMenuRequest.builder()
                    .name("양념치킨")
                    .menuGroupId(menuGroupId)
                    .price(BigDecimal.valueOf(16000L))
                    .menuProducts(List.of(MenuProductFixture.REQUEST.상품_N_M개_요청(productId, 1)))
                    .build();
        }
    }

    public static Long 메뉴_생성(CreateMenuRequest request) {
        ExtractableResponse<Response> response = RestAssured.given()
                .body(request)
                .contentType(ContentType.JSON)
                .when().post("/api/menus")
                .then().log().all()
                .statusCode(201)
                .extract();
        return Long.parseLong(response.header("Location").split("/")[3]);
    }
}
