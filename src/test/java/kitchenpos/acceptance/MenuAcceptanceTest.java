package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.CREATED;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.exception.ExceptionResponse;
import kitchenpos.ui.request.MenuProductRequest;
import kitchenpos.ui.request.MenuRequest;
import kitchenpos.ui.request.ProductRequest;
import kitchenpos.ui.response.MenuResponse;
import kitchenpos.ui.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("Menu 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {

    @DisplayName("POST /api/menus")
    @Nested
    class Post {

        @DisplayName("Price가 Null이면 상태코드 500이 반환된다.")
        @Test
        void priceNull() {
            // given
            MenuGroup menuGroup = HTTP_요청을_통해_MenuGroup을_생성한다("엄청난 그룹");

            ProductResponse 치즈버거 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 4_000);
            ProductResponse 콜라 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 1_600);

            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거, 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라, 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

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

        @DisplayName("Menu의 Price가 0보다 작으면 상태코드 500이 반환된다.")
        @Test
        void priceNegative() {
            // given
            MenuGroup menuGroup = HTTP_요청을_통해_MenuGroup을_생성한다("엄청난 그룹");

            ProductResponse 치즈버거 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 4_000);
            ProductResponse 콜라 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 1_600);

            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거, 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라, 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

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

        @DisplayName("Menu의 MenuGroupId가 존재하지 않는 경우 상태코드 500이 반환된다.")
        @Test
        void noExistMenuGroupId() {
            // given
            ProductResponse 치즈버거 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 4_000);
            ProductResponse 콜라 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 1_600);

            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거, 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라, 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);
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

        @DisplayName("Menu의 Product가 실제로 존재하지 않는 경우 상태코드 500이 반환된다.")
        @Test
        void noExistProduct() {
            // given
            MenuGroup menuGroup = HTTP_요청을_통해_MenuGroup을_생성한다("엄청난 그룹");
            ProductResponse 없는_상품 = new ProductResponse(-1L, "없는 상품", BigDecimal.valueOf(1));

            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(없는_상품, 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(없는_상품, 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

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

        @DisplayName("Menu의 총 Price가 Product들의 Price 합보다 클 경우 상태코드 500이 반환된다.")
        @Test
        void menuPriceNotMatch() {
            // given
            MenuGroup menuGroup = HTTP_요청을_통해_MenuGroup을_생성한다("엄청난 그룹");

            ProductResponse 치즈버거 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 4_000);
            ProductResponse 콜라 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 1_600);

            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거, 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라, 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

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

        @DisplayName("정상적인 경우 상태코드 201이 반환된다.")
        @Test
        void success() {
            // given
            MenuGroup menuGroup = HTTP_요청을_통해_MenuGroup을_생성한다("엄청난 그룹");

            ProductResponse 치즈버거 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 4_000);
            ProductResponse 콜라 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 1_600);

            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거, 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라, 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

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

    @DisplayName("GET /api/menus - 모든 Menu와 상태코드 200이 반환된다.")
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

    private MenuRequest MenuRequest를_생성한다(String name, int price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return MenuRequest를_생성한다(name, BigDecimal.valueOf(price), menuGroup, menuProducts);
    }

    private MenuRequest MenuRequest를_생성한다(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new MenuRequest(name, price, menuGroup.getId(), MenuProductRequest.of(menuProducts));
    }

    private MenuGroup HTTP_요청을_통해_MenuGroup을_생성한다(String name) {
        MenuGroup menuGroup = new MenuGroup(name);

        return postRequestWithBody("/api/menu-groups", menuGroup).as(MenuGroup.class);
    }

    private MenuProduct MenuProduct를_생성한다(ProductResponse productResponse, long quantity) {
        Product product = new Product(productResponse.getId(), productResponse.getName(), productResponse.getPrice());

        return new MenuProduct(product, quantity);
    }

    private ProductResponse HTTP_요청을_통해_Product를_생성한다(String name, int price) {
        ProductRequest request = new ProductRequest(name, BigDecimal.valueOf(price));

        return postRequestWithBody("/api/products", request).as(ProductResponse.class);
    }
}
