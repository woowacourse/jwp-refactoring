package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.response.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class MenuAcceptanceTest extends AcceptanceTest {

    private static final long MENU_ID = 1L;
    private static final long PRODUCT_ID = 1L;
    private static final long QUANTITY = 1L;
    private static final long MENU_GROUP_ID = 1L;

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void create() {
        // given
        final MenuProduct menuProduct = createMenuProduct();
        final Menu menu = new Menu("메뉴 이름", BigDecimal.valueOf(1_000), MENU_GROUP_ID, List.of(menuProduct));

        // when
        final Menu response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(menu)
                .when().log().all()
                .post("/api/menus")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(Menu.class);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo(menu.getName());
        assertThat(response.getPrice().longValue()).isEqualTo(menu.getPrice().longValue());
        assertThat(response.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
    }

    @DisplayName("메뉴 전체 목록을 조회할 수 있다.")
    @Test
    void list() {
        // when
        final List<MenuResponse> menus = RestAssured.given().log().all()
                .when().log().all()
                .get("/api/menus")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList(".", MenuResponse.class);

        // then
        assertThat(menus)
                .hasSize(6)
                .filteredOn(it -> it.getId() != null)
                .extracting(MenuResponse::getName, menu -> menu.getPrice().longValue(), MenuResponse::getMenuGroupId)
                .containsExactlyInAnyOrder(
                        tuple("후라이드치킨", 16_000L, 2L),
                        tuple("양념치킨", 16_000L, 2L),
                        tuple("반반치킨", 16_000L, 2L),
                        tuple("통구이", 16_000L, 2L),
                        tuple("간장치킨", 17_000L, 2L),
                        tuple("순살치킨", 17_000L, 2L)
                );
    }

    private MenuProduct createMenuProduct() {
        return new MenuProduct(MENU_ID, PRODUCT_ID, QUANTITY);
    }
}
