package kitchenpos.menu.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.common.exception.ExceptionResponse;
import kitchenpos.menu.ui.request.MenuProductRequest;
import kitchenpos.menu.ui.request.MenuRequest;
import kitchenpos.menu.ui.response.MenuResponse;
import kitchenpos.product.ui.request.ProductRequest;
import kitchenpos.product.ui.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("메뉴 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {

    @DisplayName("POST /api/menus - 메뉴를 생성할 때")
    @Nested
    class Post {

        @DisplayName("메뉴의 가격이 Null이면 예외가 발생한다.")
        @Test
        void priceNull() {
            // given
            MenuGroup menuGroup = HTTP_요청을_통해_MenuGroup을_생성한다("엄청난 그룹");

            ProductResponse 치즈버거 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 4_000);
            ProductResponse 콜라 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 1_600);

            MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(치즈버거, 1);
            MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(콜라, 1);
            List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            MenuRequest request = MenuRequest를_생성한다("엄청난 메뉴", null, menuGroup, menuProducts);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/menus")
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }

        @DisplayName("메뉴의 가격이 음수면 예외가 발생한다.")
        @Test
        void priceNegative() {
            // given
            MenuGroup menuGroup = HTTP_요청을_통해_MenuGroup을_생성한다("엄청난 그룹");

            ProductResponse 치즈버거 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 4_000);
            ProductResponse 콜라 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 1_600);

            MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(치즈버거, 1);
            MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(콜라, 1);
            List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            MenuRequest request = MenuRequest를_생성한다("엄청난 메뉴", -1, menuGroup, menuProducts);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/menus")
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }

        @DisplayName("ID와 일치하는 메뉴그룹이 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void noExistMenuGroupId() {
            // given
            ProductResponse 치즈버거 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 4_000);
            ProductResponse 콜라 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 1_600);

            MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(치즈버거, 1);
            MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(콜라, 1);
            List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);
            MenuGroup 없는_그룹 = new MenuGroup(-1L, "없는_그룹");

            MenuRequest request = MenuRequest를_생성한다("엄청난 메뉴", 5_600, 없는_그룹, menuProducts);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/menus")
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }

        @DisplayName("ID에 일치하는 상품이 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void noExistProduct() {
            // given
            MenuGroup menuGroup = HTTP_요청을_통해_MenuGroup을_생성한다("엄청난 그룹");
            ProductResponse 없는_상품 = new ProductResponse(-1L, "없는 상품", BigDecimal.valueOf(1));

            MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(없는_상품, 1);
            MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(없는_상품, 1);
            List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            MenuRequest request = MenuRequest를_생성한다("엄청난 메뉴", 5_600, menuGroup, menuProducts);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/menus")
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }

        @DisplayName("[메뉴의 가격]이 [모든 상품의 가격 * 개수의 합]보다 클 경우 예외가 발생한다.")
        @Test
        void menuPriceNotMatch() {
            // given
            MenuGroup menuGroup = HTTP_요청을_통해_MenuGroup을_생성한다("엄청난 그룹");

            ProductResponse 치즈버거 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 4_000);
            ProductResponse 콜라 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 1_600);

            MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(치즈버거, 1);
            MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(콜라, 1);
            List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            MenuRequest request = MenuRequest를_생성한다("엄청난 메뉴", 5_601, menuGroup, menuProducts);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/menus")
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }

        @DisplayName("정상적인 경우 메뉴 생성에 성공한다.")
        @Test
        void success() {
            // given
            MenuGroup menuGroup = HTTP_요청을_통해_MenuGroup을_생성한다("엄청난 그룹");

            ProductResponse 치즈버거 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 4_000);
            ProductResponse 콜라 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 1_600);

            MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(치즈버거, 1);
            MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(콜라, 1);
            List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            MenuRequest request = MenuRequest를_생성한다("엄청난 메뉴", 5_600, menuGroup, menuProducts);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/menus")
                .then().log().all()
                .statusCode(CREATED.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(CREATED.value());
            assertThat(response.header("Location")).isNotNull();
            assertThat(response.body()).isNotNull();
        }
    }

    @DisplayName("GET /api/menus - 모든 메뉴 목록을 반환 받는다.")
    @Test
    void list() {
        // when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .when().get("/api/menus")
            .then().log().all()
            .statusCode(OK.value())
            .extract();

        List<MenuResponse> responses = response.jsonPath().getList(".", MenuResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(responses).isNotNull();
    }

    private MenuRequest MenuRequest를_생성한다(String name, int price, MenuGroup menuGroup, List<MenuProductRequest> menuProducts) {
        return MenuRequest를_생성한다(name, BigDecimal.valueOf(price), menuGroup, menuProducts);
    }

    private MenuRequest MenuRequest를_생성한다(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProductRequest> menuProducts) {
        return new MenuRequest(name, price, menuGroup.getId(), menuProducts);
    }

    private MenuGroup HTTP_요청을_통해_MenuGroup을_생성한다(String name) {
        MenuGroup menuGroup = new MenuGroup(name);

        return postRequestWithBody("/api/menu-groups", menuGroup).as(MenuGroup.class);
    }

    private MenuProductRequest MenuProductRequest를_생성한다(ProductResponse productResponse, long quantity) {
        return new MenuProductRequest(productResponse.getId(), quantity);
    }

    private ProductResponse HTTP_요청을_통해_Product를_생성한다(String name, int price) {
        ProductRequest request = new ProductRequest(name, BigDecimal.valueOf(price));

        return postRequestWithBody("/api/products", request).as(ProductResponse.class);
    }
}
