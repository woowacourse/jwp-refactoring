package acceptance;

import static fixture.MenuFixtures.간장치킨_메뉴;
import static fixture.MenuFixtures.반반치킨_메뉴;
import static fixture.MenuFixtures.순살치킨_메뉴;
import static fixture.MenuFixtures.양념치킨_메뉴;
import static fixture.MenuFixtures.통구이_메뉴;
import static fixture.MenuFixtures.후라이드치킨_메뉴;
import static fixture.ProductFixtures.양념치킨_상품;
import static fixture.ProductFixtures.후라이드_상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import common.AcceptanceTest;
import io.restassured.RestAssured;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.ui.request.MenuProductRequest;
import kitchenpos.ui.request.MenuRequest;
import kitchenpos.ui.response.MenuProductResponse;
import kitchenpos.ui.response.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class MenuAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void findMenuList() {
        // act
        List<MenuResponse> menus = getMenus();

        // assert
        assertThat(menus)
                .extracting(MenuResponse::getId, MenuResponse::getName, MenuResponse::getMenuGroupId, m -> m.getPrice().intValueExact())
                .hasSize(6)
                .contains(
                        tuple(후라이드치킨_메뉴.id(), 후라이드치킨_메뉴.이름(), 후라이드치킨_메뉴.그룹_ID(), 후라이드치킨_메뉴.가격()),
                        tuple(양념치킨_메뉴.id(), 양념치킨_메뉴.이름(), 양념치킨_메뉴.그룹_ID(), 양념치킨_메뉴.가격()),
                        tuple(반반치킨_메뉴.id(), 반반치킨_메뉴.이름(), 반반치킨_메뉴.그룹_ID(), 반반치킨_메뉴.가격()),
                        tuple(통구이_메뉴.id(), 통구이_메뉴.이름(), 통구이_메뉴.그룹_ID(), 통구이_메뉴.가격()),
                        tuple(간장치킨_메뉴.id(), 간장치킨_메뉴.이름(), 간장치킨_메뉴.그룹_ID(), 간장치킨_메뉴.가격()),
                        tuple(순살치킨_메뉴.id(), 순살치킨_메뉴.이름(), 순살치킨_메뉴.그룹_ID(), 순살치킨_메뉴.가격())
                );
    }

    @DisplayName("메뉴를 추가한다.")
    @Test
    void createMenu() {
        // arrange
        MenuProductRequest 후라이드 = createMenuProductRequest(후라이드_상품.id(), 1L);
        MenuProductRequest 양념 = createMenuProductRequest(양념치킨_상품.id(), 1L);

        // act
        MenuResponse createdMenu = createMenu("후라이드+양념치킨", 19000, 1L, 후라이드, 양념);

        // assert
        assertThat(createdMenu.getId()).isNotNull();
        assertThat(createdMenu.getName()).isEqualTo("후라이드+양념치킨");
        assertThat(createdMenu.getPrice().intValueExact()).isEqualTo(19000);
        assertThat(createdMenu.getMenuGroupId()).isEqualTo(1L);
        assertThat(createdMenu.getMenuProducts())
                .extracting(MenuProductResponse::getProductId, MenuProductResponse::getQuantity)
                .containsExactlyInAnyOrder(
                        tuple(후라이드_상품.id(), 1L),
                        tuple(양념치킨_상품.id(), 1L)
                );

        List<MenuResponse> menus = getMenus();
        assertThat(menus)
                .extracting(MenuResponse::getId)
                .hasSize(7)
                .contains(createdMenu.getId());
    }

    private MenuProductRequest createMenuProductRequest(long id, long quantity) {
        return new MenuProductRequest(id, quantity);
    }

    private MenuResponse createMenu(String name, int price, long menuGroupId, MenuProductRequest... menuProductRequests) {
        MenuRequest menuRequest = new MenuRequest(name, BigDecimal.valueOf(price), menuGroupId,
                List.of(menuProductRequests));

        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(menuRequest)
                .when().log().all()
                .post("/api/menus")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(MenuResponse.class);
    }

    private List<MenuResponse> getMenus() {
        return RestAssured.given().log().all()
                .when().log().all()
                .get("/api/menus")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getList(".", MenuResponse.class);
    }
}
