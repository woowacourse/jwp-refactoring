package kitchenpos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuIntegrationTest extends IntegrationTest {

    @BeforeEach
    @Override
    void setUp() {
        super.setUp();
        steps.createMenuGroup(MenuGroupFixture.LUNCH.toEntity());
        steps.createProduct(ProductFixture.FRIED_CHICKEN.toEntity());
    }

    @Nested
    @DisplayName("메뉴를 등록할 수 있다.")
    class create_success {

        @Test
        @DisplayName("메뉴 가격이 상품 가격 총합과 같을 때(최대값)")
        void max_price() {
            // given
            MenuProduct menuProduct = MenuProductFixture.FRIED_CHICKEN_MENU_PRODUCT.toEntity();

            Menu expected = MenuFixture.computeDefaultMenu(arg ->
                arg.setPrice(BigDecimal.valueOf(ProductFixture.FRIED_CHICKEN.price.longValue() * menuProduct.getQuantity()))
            );

            // when
            steps.createMenu(expected);
            Menu actual = sharedContext.getResponse().as(Menu.class);

            // then
            assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getPrice().longValue()).isEqualTo(expected.getPrice().longValue())
            );
        }

        @Test
        @DisplayName("메뉴 가격이 0원일 때(최소값)")
        void min_price() {
            // given
            Menu expected = MenuFixture.computeDefaultMenu(arg ->
                arg.setPrice(BigDecimal.valueOf(0))
            );

            // when
            steps.createMenu(expected);
            Menu actual = sharedContext.getResponse().as(Menu.class);

            // then
            assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getPrice().longValue()).isEqualTo(expected.getPrice().longValue())
            );
        }

        @Test
        @DisplayName("메뉴가 이미 등록된 메뉴와 이름이 같을 때")
        void same_name() {
            // given
            Menu expected = MenuFixture.computeDefaultMenu(ignored -> {});

            // when
            steps.createMenu(expected);
            steps.createMenu(expected);
            ExtractableResponse<Response> response = sharedContext.getResponse();

            // then
            assertThat(response.statusCode()).isEqualTo(201);
        }

        @Test
        @DisplayName("메뉴가 이미 등록된 메뉴와 같은 메뉴 상품을 가질 때")
        void same_menuproduct() {
            // given
            MenuProduct menuProduct = MenuProductFixture.FRIED_CHICKEN_MENU_PRODUCT.toEntity();

            Menu menu1 = MenuFixture.computeDefaultMenu(arg ->
                arg.setMenuProducts(List.of(menuProduct))
            );
            Menu menu2 = MenuFixture.computeDefaultMenu(arg ->
                arg.setMenuProducts(List.of(menuProduct))
            );

            // when
            steps.createMenu(menu1);
            steps.createMenu(menu2);
            ExtractableResponse<Response> response = sharedContext.getResponse();

            // then
            assertThat(response.statusCode()).isEqualTo(201);
        }
    }

    @Nested
    @DisplayName("메뉴를 등록할 수 없다.")
    class create_failure {

        @Test
        @DisplayName("메뉴 가격이 0보다 작을 때")
        void price_lower_than_0() {
            // given
            Menu menu = MenuFixture.computeDefaultMenu(arg ->
                arg.setPrice(BigDecimal.valueOf(-1L))
            );

            // when
            steps.createMenu(menu);
            ExtractableResponse<Response> response = sharedContext.getResponse();

            // then
            assertThat(response.statusCode()).isEqualTo(500); // Assuming you want to return a 500 error for invalid input
        }

        @Test
        @DisplayName("메뉴 그룹이 없을 때")
        void menugroup_not_exists() {
            // given
            Menu menu = MenuFixture.computeDefaultMenu(arg ->
                arg.setMenuGroupId(-1L)
            );

            // when
            steps.createMenu(menu);
            ExtractableResponse<Response> response = sharedContext.getResponse();

            // then
            assertThat(response.statusCode()).isEqualTo(500); // Assuming you want to return a 500 error for invalid input
        }

        @Test
        @DisplayName("메뉴 가격이 메뉴 상품 가격 총합보다 클 때")
        void price_bigger_than_product_price() {
            // given
            MenuProduct menuProduct = MenuProductFixture.FRIED_CHICKEN_MENU_PRODUCT.toEntity();

            Menu menu = MenuFixture.computeDefaultMenu(arg ->
                arg.setPrice(BigDecimal.valueOf(ProductFixture.FRIED_CHICKEN.price.longValue() * menuProduct.getQuantity() + 1L))
            );

            // when
            steps.createMenu(menu);
            ExtractableResponse<Response> response = sharedContext.getResponse();

            // then
            assertThat(response.statusCode()).isEqualTo(500);
        }
    }

    @Test
    @DisplayName("메뉴 목록을 조회할 수 있다.")
    void listMenus_success() {
        // when
        List<Menu> actual = RestAssured.given().log().all()
                                       .get("/api/menus")
                                       .then().log().all()
                                       .extract()
                                       .jsonPath().getList(".", Menu.class);

        // then
        assertThat(actual).isEmpty();
    }
}
