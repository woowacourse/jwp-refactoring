package kitchenpos.fixture;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.dto.request.CreateMenuGroupRequest;
import kitchenpos.dto.request.CreateMenuRequest;
import kitchenpos.dto.response.CreateMenuResponse;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.domain.menu.Menu;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.MenuGroupFixture.MENU_GROUP;
import static kitchenpos.fixture.MenuProductFixture.MENU_PRODUCT;

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

        public static CreateMenuRequest A_상품_B_원_C_그룹_메뉴_등록_요청(Long productId, Long price, Long menuGroupId) {
            return CreateMenuRequest.builder()
                    .name("후라이드치킨")
                    .menuGroupId(menuGroupId)
                    .price(BigDecimal.valueOf(price))
                    .menuProducts(List.of(MenuProductFixture.REQUEST.상품_N_M개_요청(productId, 1)))
                    .build();
        }
    }

    public static class RESPONSE {

        public static CreateMenuResponse 후라이드_치킨_생성_응답() {
            return CreateMenuResponse.builder()
                    .id(1L)
                    .name("후라이드치킨")
                    .price(BigDecimal.valueOf(16000L))
                    .menuGroupId(1L)
                    .menuProducts(List.of())
                    .build();
        }

        public static MenuResponse 후라이드_치킨() {
            return MenuResponse.builder()
                    .id(1L)
                    .name("후라이드치킨")
                    .price(BigDecimal.valueOf(16000L))
                    .menuGroupId(1L)
                    .menuProducts(List.of(MenuProductFixture.RESPONSE.후라이드_치킨_1마리_응답()))
                    .build();
        }
    }

    public static class MENU {

        public static Menu 후라이드_치킨_16000원_1마리() {
            return Menu.builder()
                    .id(1L)
                    .name("후라이드치킨")
                    .price(BigDecimal.valueOf(16000L))
                    .menuGroupId(MENU_GROUP.메뉴_그룹_치킨().getId())
                    .menuProducts(List.of(MENU_PRODUCT.후라이드_치킨_1마리()))
                    .build();
        }

        public static Menu 후라이드_치킨_N원_1마리(long price) {
            return Menu.builder()
                    .id(1L)
                    .name("후라이드치킨")
                    .price(BigDecimal.valueOf(price))
                    .menuGroupId(MENU_GROUP.메뉴_그룹_치킨().getId())
                    .menuProducts(List.of(MENU_PRODUCT.후라이드_치킨_1마리()))
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
