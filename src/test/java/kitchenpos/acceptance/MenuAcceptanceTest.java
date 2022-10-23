package kitchenpos.acceptance;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("메뉴를 등록한다.")
    void createMenu() {
        // given
        Menu menu = new Menu("후라이드+후라이드", new BigDecimal(32000), 1L, List.of(new MenuProduct(1L, 2)));

        // when, then
        _메뉴등록검증(menu);
    }

    @Test
    @DisplayName("전체 메뉴를 조회한다.")
    void findAll() {
        // given
        Menu menu1 = new Menu("후라이드+후라이드", new BigDecimal(32000), 1L, List.of(new MenuProduct(1L, 2)));
        Menu menu2 = new Menu("양념치킨+양념치킨", new BigDecimal(32000), 1L, List.of(new MenuProduct(2L, 2)));

        _메뉴등록검증(menu1);
        _메뉴등록검증(menu2);

        // when, then
        _메뉴조회검증();
    }

    private void _메뉴등록검증(final Menu menu) {
        post("/api/menus", menu).assertThat()
            .statusCode(HttpStatus.CREATED.value());
    }

    private void _메뉴조회검증() {
        get("api/menus").assertThat()
            .statusCode(HttpStatus.OK.value());
    }
}
