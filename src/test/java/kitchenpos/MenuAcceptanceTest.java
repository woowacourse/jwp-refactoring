package kitchenpos;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.MenuGroupFixture.일식;
import static kitchenpos.fixture.ProductFixture.스키야키;
import static kitchenpos.step.MenuGroupStep.toRequest;
import static kitchenpos.step.MenuGroupStep.메뉴_그룹_생성_요청하고_아이디_반환;
import static kitchenpos.step.MenuStep.toMenuRequest;
import static kitchenpos.step.MenuStep.메뉴_생성_요청;
import static kitchenpos.step.MenuStep.메뉴_생성_요청하고_메뉴_반환;
import static kitchenpos.step.MenuStep.메뉴_조회_요청;
import static kitchenpos.step.ProductStep.toRequest;
import static kitchenpos.step.ProductStep.상품_생성_요청하고_아이디_반환;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

class MenuAcceptanceTest extends AcceptanceTest {

    @Nested
    class MenuCreateTest {

        @Test
        void 메뉴를_생성한다() {
            final MenuGroup menuGroup = 일식();
            final Long menuGroupId = 메뉴_그룹_생성_요청하고_아이디_반환(toRequest(menuGroup));
            final Long productId = 상품_생성_요청하고_아이디_반환(toRequest(스키야키()));

            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(productId);
            menuProduct.setQuantity(1L);

            final Menu menu = new Menu();
            menu.setName("스키야키");
            menu.setPrice(BigDecimal.valueOf(11_900));
            menu.setMenuGroupId(menuGroupId);
            menu.setMenuProducts(List.of(menuProduct));

            final ExtractableResponse<Response> response = 메뉴_생성_요청(toMenuRequest(menu));

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(CREATED.value()),
                    () -> assertThat(response.jsonPath().getString("name")).isEqualTo(menu.getName())
            );
        }

        @Test
        void 메뉴는_반드시_메뉴_그룹에_속해야_한다() {
            final Product product = 스키야키();
            final Long productId = 상품_생성_요청하고_아이디_반환(toRequest(product));

            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(productId);
            menuProduct.setQuantity(1L);

            final Menu menu = new Menu();
            menu.setName(product.getName());
            menu.setPrice(product.getPrice());
            menu.setMenuProducts(List.of(menuProduct));

            final ExtractableResponse<Response> response = 메뉴_생성_요청(toMenuRequest(menu));

            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @Test
        void 메뉴의_가격은_메뉴에_속하는_상품_곱하기_수량의_합_이하여야_한다() {
            final MenuGroup menuGroup = 일식();
            final Long menuGroupId = 메뉴_그룹_생성_요청하고_아이디_반환(toRequest(menuGroup));

            final Product product = 스키야키();
            final Long productId = 상품_생성_요청하고_아이디_반환(toRequest(product));

            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(productId);
            menuProduct.setQuantity(2L);

            final Menu menu = new Menu();
            menu.setName(product.getName());
            menu.setPrice(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())).add(BigDecimal.TEN));
            menu.setMenuGroupId(menuGroupId);
            menu.setMenuProducts(List.of(menuProduct));

            final ExtractableResponse<Response> response = 메뉴_생성_요청(toMenuRequest(menu));

            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }
    }

    @Test
    void 메뉴를_조회한다() {
        final MenuGroup menuGroup = 일식();
        final Long menuGroupId = 메뉴_그룹_생성_요청하고_아이디_반환(toRequest(menuGroup));
        final Long productId = 상품_생성_요청하고_아이디_반환(toRequest(스키야키()));

        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(1L);

        final Menu menu = new Menu();
        menu.setName("스키야키");
        menu.setPrice(BigDecimal.valueOf(11_900));
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(List.of(menuProduct));

        final Menu createdMenu = 메뉴_생성_요청하고_메뉴_반환(toMenuRequest(menu));

        final ExtractableResponse<Response> response = 메뉴_조회_요청();
        final List<Menu> result = response.jsonPath().getList("", Menu.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result.get(0))
                        .usingRecursiveComparison()
                        .ignoringFields("price")
                        .isEqualTo(createdMenu)
        );
    }
}
