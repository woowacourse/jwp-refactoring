package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.Arrays;
import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProductCreateRequest;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    private Product product;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        Product newProduct = Product.of("치킨", 18_000L);

        MenuGroup newMenuGroup = MenuGroup.from("튀김류");

        product = productDao.save(newProduct);
        menuGroup = menuGroupDao.save(newMenuGroup);
    }

    @Nested
    class 메뉴_생성 {

        @Test
        void 정상_요청() {
            // given
            MenuProductCreateRequest menuProductRequest = new MenuProductCreateRequest(product.getId(), 1L);
            MenuCreateRequest request = createMenuRequest("치킨 단품", 18_000L, menuGroup.getId(), menuProductRequest);

            // when
            Menu savedMenu = menuService.create(request);

            // then
            assertSoftly(
                    softly -> {
                        softly.assertThat(savedMenu.getMenuGroupId()).isEqualTo(request.getMenuGroupId());
                        softly.assertThat(savedMenu.getName()).isEqualTo(request.getName());
                        softly.assertThat(savedMenu.getPrice().longValue()).isEqualTo(request.getPrice());
                        softly.assertThat(savedMenu.getMenuProducts().get(0))
                                .usingRecursiveComparison()
                                .comparingOnlyFields("productId", "quantity")
                                .isEqualTo(menuProductRequest.to());
                    }
            );
        }

        @ParameterizedTest
        @ValueSource(longs = {-10_000L, -18_000L})
        void 요청_가격이_0미만이면_예외_발생(long price) {
            // given
            MenuProductCreateRequest menuProductRequest = new MenuProductCreateRequest(product.getId(), 1L);
            MenuCreateRequest request = createMenuRequest("치킨 단품", price, menuGroup.getId(), menuProductRequest);

            // when, then
            assertThatThrownBy(
                    () -> menuService.create(request)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청_가격이_null이면_예외_발생() {
            // given
            Long wrongPrice = null;
            MenuProductCreateRequest menuProductRequest = new MenuProductCreateRequest(product.getId(), 1L);
            MenuCreateRequest request = createMenuRequest("치킨 단품", wrongPrice, menuGroup.getId(), menuProductRequest);

            // when, then
            assertThatThrownBy(
                    () -> menuService.create(request)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청_상품이_등록된_상품이_아니면_예외_발생() {
            // given
            long invalidProductId = -1L;
            MenuProductCreateRequest menuProductRequest = new MenuProductCreateRequest(invalidProductId, 1L);
            MenuCreateRequest request = createMenuRequest("치킨 단품", 18_000L, menuGroup.getId(), menuProductRequest);

            // when, then
            assertThatThrownBy(
                    () -> menuService.create(request)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청_가격이_메뉴_상품들_가격의_합보다_크면_예외_발생() {
            // given
            Long wrongPrice = product.getPrice().longValue() + 10_000L;
            MenuProductCreateRequest menuProductRequest = new MenuProductCreateRequest(product.getId(), 1L);
            MenuCreateRequest request = createMenuRequest("치킨 단품", wrongPrice, menuGroup.getId(), menuProductRequest);

            // when, then
            assertThatThrownBy(
                    () -> menuService.create(request)
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 메뉴_전체_조회 {

        @Test
        void 정상_요청() {
            // given
            MenuProductCreateRequest menuProductRequest = new MenuProductCreateRequest(product.getId(), 1L);
            MenuCreateRequest request = createMenuRequest("치킨 단품", 18_000L, menuGroup.getId(), menuProductRequest);
            Menu savedMenu = menuService.create(request);

            // when
            List<Menu> menus = menuService.readAll();

            // then
            assertThat(menus)
                    .extracting(Menu::getId)
                    .contains(savedMenu.getId());
        }
    }

    private MenuCreateRequest createMenuRequest(final String name,
                                                final Long price,
                                                final Long menuGroupId,
                                                final MenuProductCreateRequest... menuProducts) {
        return new MenuCreateRequest(name, price, menuGroupId, Arrays.asList(menuProducts));
    }
}
