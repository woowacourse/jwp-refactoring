package kitchenpos.menu.acceptance;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import kitchenpos.common.AcceptanceTest;
import kitchenpos.menu.application.request.MenuProductRequest;
import kitchenpos.menu.application.request.MenuRequest;
import kitchenpos.menu.application.request.MenuGroupRequest;
import kitchenpos.product.application.request.ProductCreateRequest;

public class MenuAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("메뉴를 등록한다.")
    void createMenu() {
        // given
        MenuRequest request = createMenuFixture();

        // when, then
        _메뉴등록검증(request);
    }

    @Test
    @DisplayName("전체 메뉴를 조회한다.")
    void findAll() {
        // given
        MenuRequest request1 = createMenuFixture();
        MenuRequest request2 = createMenuFixture();

        _메뉴등록검증(request1);
        _메뉴등록검증(request1);

        // when, then
        _메뉴조회검증();
    }

    private void _메뉴등록검증(final MenuRequest request) {
        post("/api/menus", request).assertThat()
            .statusCode(HttpStatus.CREATED.value());
    }

    private void _메뉴조회검증() {
        get("api/menus").assertThat()
            .statusCode(HttpStatus.OK.value());
    }

    private MenuRequest createMenuFixture() {
        MenuGroupRequest menuGroup = new MenuGroupRequest(NO_ID, "세마리메뉴");
        long menuGroupId = _메뉴그룹등록_Id반환(menuGroup);

        ProductCreateRequest product = new ProductCreateRequest("후라이드", BigDecimal.valueOf(16000));
        long productId = _상품등록_Id반환(product);

        List<MenuProductRequest> menuProducts = List.of(new MenuProductRequest(NO_ID, NO_ID, productId, 3));
        return new MenuRequest(NO_ID, "후라이드+후라이드+후라이드", new BigDecimal(48000), menuGroupId, menuProducts);
    }
}
