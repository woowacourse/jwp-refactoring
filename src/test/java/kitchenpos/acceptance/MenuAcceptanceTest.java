package kitchenpos.acceptance;

import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class MenuAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("메뉴를 추가한다")
    void createMenu() {
        // given
        final Menu requestBody = Menu.create("런치세트", 15000L, 세트.getId(), List.of(토마토파스타.getId(), 탄산음료.getId()));

        final ExtractableResponse<Response> response = 메뉴_등록_요청(requestBody);
        final Menu responseBody = response.body().as(Menu.class);
        final List<MenuProduct> menuProducts = responseBody.getMenuProducts();

        // then
        assertAll(
                () -> 응답_코드_일치_검증(response, HttpStatus.CREATED),
                () -> 단일_데이터_검증(responseBody.getName(), requestBody.getName()),
                //() -> 단일_데이터_검증(responseBody.getPrice(), requestBody.getPrice()),
                () -> 단일_데이터_검증(responseBody.getMenuGroupId(), 세트.getId()),
                () -> 리스트_데이터_검증(menuProducts, "menuId", responseBody.getId(), responseBody.getId()),
                () -> 리스트_데이터_검증(menuProducts, "productId", 토마토파스타.getId(), 탄산음료.getId()),
                () -> 리스트_데이터_검증(menuProducts, "quantity", 1L, 1L)
        );
    }

    @Test
    @DisplayName("전체 메뉴를 조회할 수 있다")
    void getMenus() {
        // given
        final Menu menu1 = 메뉴_등록("파스타세트", 16000L, 세트, 토마토파스타.getId(), 탄산음료.getId());
        final Menu menu2 = 메뉴_등록("목살스테이크세트", 21000L, 세트, 목살스테이크.getId(), 탄산음료.getId());

        // when
        final ExtractableResponse<Response> response = 모든_메뉴_조회_요청();
        final List<Menu> responseBody = response.body()
                .jsonPath()
                .getList(".", Menu.class);

        // then
        assertAll(
                () -> 응답_코드_일치_검증(response, HttpStatus.OK),
                () -> 리스트_데이터_검증(responseBody, "id", menu1.getId(), menu2.getId()),
                () -> 리스트_데이터_검증(responseBody, "name", menu1.getName(), menu2.getName()),
                //() -> 리스트_데이터_검증(responseBody, "price", menu1.getPrice().doubleValue(), menu2.getPrice().doubleValue()),
                () -> 리스트_데이터_검증(responseBody, "menuGroupId", 세트.getId(), 세트.getId())
        );
    }
}
