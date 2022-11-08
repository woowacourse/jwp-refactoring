package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.application.request.MenuProductRequest;
import kitchenpos.menu.application.request.MenuRequest;
import kitchenpos.menu.application.response.MenuResponse;
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
        final MenuRequest menuRequest = new MenuRequest("메뉴 이름", BigDecimal.valueOf(1_000), MENU_GROUP_ID,
                createMenuProducts());

        // when
        final MenuResponse response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(menuRequest)
                .when().log().all()
                .post("/api/menus")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(MenuResponse.class);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo(menuRequest.getName());
        assertThat(response.getPrice().longValue()).isEqualTo(menuRequest.getPrice().longValue());
        assertThat(response.getMenuGroupId()).isEqualTo(menuRequest.getMenuGroupId());
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

    private List<MenuProductRequest> createMenuProducts() {
        return List.of(new MenuProductRequest(MENU_ID, PRODUCT_ID, QUANTITY));
    }
}
