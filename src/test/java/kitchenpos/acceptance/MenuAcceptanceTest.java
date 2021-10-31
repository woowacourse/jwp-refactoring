package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("Menu 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @DisplayName("POST /api/menu-groups")
    @Nested
    class Post {

        @DisplayName("Price가 Null이면 상태코드 500이 반환된다.")
        @Test
        void priceNull() {
            // given
            MenuGroup menuGroup = menuGroupDao.save(MenuGroup을_생성한다("엄청난 그룹"));

            Product 치즈버거 = productDao.save(Product를_생성한다("치즈버거", 4_000));
            Product 콜라 = productDao.save(Product를_생성한다("치즈버거", 1_600));

            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거.getId(), 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라.getId(), 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            Menu menu = Menu를_생성한다("엄청난 메뉴", null, menuGroup.getId(), menuProducts);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menu)
                .when().post("/api/menus")
                .then().log().all()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @DisplayName("Menu의 Price가 0보다 작으면 상태코드 500이 반환된다.")
        @Test
        void priceNegative() {
            // given
            MenuGroup menuGroup = menuGroupDao.save(MenuGroup을_생성한다("엄청난 그룹"));

            Product 치즈버거 = productDao.save(Product를_생성한다("치즈버거", 4_000));
            Product 콜라 = productDao.save(Product를_생성한다("치즈버거", 1_600));

            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거.getId(), 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라.getId(), 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            Menu menu = Menu를_생성한다("엄청난 메뉴", -1, menuGroup.getId(), menuProducts);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menu)
                .when().post("/api/menus")
                .then().log().all()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @DisplayName("Menu의 MenuGroupId가 존재하지 않는 경우 상태코드 500이 반환된다.")
        @Test
        void noExistMenuGroupId() {
            // given
            Product 치즈버거 = productDao.save(Product를_생성한다("치즈버거", 4_000));
            Product 콜라 = productDao.save(Product를_생성한다("치즈버거", 1_600));

            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거.getId(), 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라.getId(), 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            Menu menu = Menu를_생성한다("엄청난 메뉴", 5_600, -1L, menuProducts);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menu)
                .when().post("/api/menus")
                .then().log().all()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @DisplayName("Menu의 Product가 실제로 존재하지 않는 경우 상태코드 500이 반환된다.")
        @Test
        void noExistProduct() {
            // given
            MenuGroup menuGroup = menuGroupDao.save(MenuGroup을_생성한다("엄청난 그룹"));

            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(-1L, 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(-2L, 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            Menu menu = Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menu)
                .when().post("/api/menus")
                .then().log().all()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @DisplayName("Menu의 총 Price가 Product들의 Price 합보다 클 경우 상태코드 500이 반환된다.")
        @Test
        void menuPriceNotMatch() {
            // given
            MenuGroup menuGroup = menuGroupDao.save(MenuGroup을_생성한다("엄청난 그룹"));

            Product 치즈버거 = productDao.save(Product를_생성한다("치즈버거", 4_000));
            Product 콜라 = productDao.save(Product를_생성한다("치즈버거", 1_600));

            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거.getId(), 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라.getId(), 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            Menu menu = Menu를_생성한다("엄청난 메뉴", 5_601, menuGroup.getId(), menuProducts);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menu)
                .when().post("/api/menus")
                .then().log().all()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @DisplayName("정상적인 경우 상태코드 201이 반환된다.")
        @Test
        void success() {
            // given
            MenuGroup menuGroup = menuGroupDao.save(MenuGroup을_생성한다("엄청난 그룹"));

            Product 치즈버거 = productDao.save(Product를_생성한다("치즈버거", 4_000));
            Product 콜라 = productDao.save(Product를_생성한다("치즈버거", 1_600));

            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거.getId(), 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라.getId(), 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            Menu menu = Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menu)
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

        List<Menu> menus = response.jsonPath().getList(".", Menu.class);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(menus).isNotNull();
    }

    private Menu Menu를_생성한다(String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return Menu를_생성한다(name, BigDecimal.valueOf(price), menuGroupId, menuProducts);
    }

    private Menu Menu를_생성한다(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);

        return menu;
    }

    private MenuGroup MenuGroup을_생성한다(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);

        return menuGroup;
    }

    private MenuProduct MenuProduct를_생성한다(Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    private Product Product를_생성한다(String name, int price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));

        return product;
    }
}
