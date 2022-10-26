package kitchenpos.acceptance;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("메뉴를 등록한다.")
    void createMenu() {
        // given
        Menu menu = createMenuFixture();

        // when, then
        _메뉴등록검증(menu);
    }

    @Test
    @DisplayName("전체 메뉴를 조회한다.")
    void findAll() {
        // given
        Menu menu1 = createMenuFixture();
        Menu menu2 = createMenuFixture();

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

    private Menu createMenuFixture() {
        MenuGroup menuGroup = new MenuGroup("세마리메뉴");
        long menuGroupId = _메뉴그룹등록_Id반환(menuGroup);

        Product product = new Product("후라이드", BigDecimal.valueOf(16000));
        long productId = _상품등록_Id반환(product);

        return new Menu("후라이드+후라이드+후라이드", new BigDecimal(48000), menuGroupId,
            List.of(new MenuProduct(productId, 3)));
    }
}
