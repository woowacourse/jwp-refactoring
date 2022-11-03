package kitchenpos.acceptance;

import io.restassured.response.ValidatableResponse;
import java.util.Collections;
import java.util.List;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.menu.MenuGroup;
import kitchenpos.menu.MenuProduct;
import kitchenpos.menu.Product;
import kitchenpos.support.RequestBuilder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("메뉴 관련 api")
public class MenuAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        // given
        final int price = 4000;
        final Product savedProduct = dataSupport.saveProduct("참치마요", price);
        final MenuGroup savedMenuGroup = dataSupport.saveMenuGroup("할인 상품");
        final List<MenuProduct> menuProducts = Collections.singletonList(MenuProduct.ofUnsaved(null, savedProduct, 1L));
        final int discountedPrice = price - 500;

        // when
        final MenuRequest request = RequestBuilder.ofMenu(savedMenuGroup, menuProducts, discountedPrice);
        final ValidatableResponse response = post("/api/menus", request);

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header("Location", Matchers.notNullValue());
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        // given, when
        final ValidatableResponse response = get("/api/menus");

        // then
        response.statusCode(HttpStatus.OK.value());
    }
}
