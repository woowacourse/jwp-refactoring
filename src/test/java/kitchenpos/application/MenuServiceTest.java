package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
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
        Product newProduct = new Product("치킨", BigDecimal.valueOf(18_000L));

        MenuGroup newMenuGroup = new MenuGroup();
        newMenuGroup.setName("튀김류");

        product = productDao.save(newProduct);
        menuGroup = menuGroupDao.save(newMenuGroup);
    }

    @Nested
    class 메뉴_생성 {

        @Test
        void 정상_요청() {
            // given
            MenuProduct menuProduct = createMenuProduct(product.getId(), 1);
            Menu menu = createMenu("치킨 단품",
                    18_000L,
                    menuGroup.getId(),
                    menuProduct);

            // when
            Menu savedMenu = menuService.create(menu);

            // then
            assertSoftly(
                    softly -> {
                        softly.assertThat(savedMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
                        softly.assertThat(savedMenu.getName()).isEqualTo(menu.getName());
                        softly.assertThat(savedMenu.getPrice()).isEqualByComparingTo(menu.getPrice());
                        softly.assertThat(savedMenu.getMenuProducts().get(0))
                                .usingRecursiveComparison()
                                .ignoringFields("seq")
                                .isEqualTo(menuProduct);
                    }
            );
        }

        @ParameterizedTest
        @ValueSource(longs = {-10_000L, -18_000L})
        void 요청_가격이_0미만이면_예외_발생(long price) {
            // given
            MenuProduct menuProduct = createMenuProduct(product.getId(), 1);
            Menu menu = createMenu("치킨 단품",
                    price,
                    menuGroup.getId(),
                    menuProduct);

            // when, then
            assertThatThrownBy(
                    () -> menuService.create(menu)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청_가격이_null이면_예외_발생() {
            // given
            Long wrongPrice = null;
            MenuProduct menuProduct = createMenuProduct(product.getId(), 1);
            Menu menu = createMenu("치킨 단품",
                    wrongPrice,
                    menuGroup.getId(),
                    menuProduct);

            // when, then
            assertThatThrownBy(
                    () -> menuService.create(menu)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청_상품이_등록된_상품이_아니면_예외_발생() {
            // given
            long invalidProductId = -1L;
            MenuProduct menuProduct = createMenuProduct(invalidProductId, 1);
            Menu menu = createMenu("치킨 단품",
                    18_000L,
                    menuGroup.getId(),
                    menuProduct);

            // when, then
            assertThatThrownBy(
                    () -> menuService.create(menu)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청_가격이_메뉴_상품들_가격의_합보다_크면_예외_발생() {
            // given
            Long wrongPrice = product.getPrice().longValue() + 10_000L;
            MenuProduct menuProduct = createMenuProduct(product.getId(), 1);
            Menu menu = createMenu("치킨 단품",
                    wrongPrice,
                    menuGroup.getId(),
                    menuProduct);

            // when, then
            assertThatThrownBy(
                    () -> menuService.create(menu)
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 메뉴_전체_조회 {

        @Test
        void 정상_요청() {
            // given
            MenuProduct menuProduct = createMenuProduct(product.getId(), 1);
            Menu menu = createMenu("치킨 단품",
                    18_000L,
                    menuGroup.getId(),
                    menuProduct);
            Menu savedMenu = menuService.create(menu);

            // when
            List<Menu> menus = menuService.readAll();

            // then
            assertThat(menus)
                    .extracting(Menu::getId)
                    .contains(savedMenu.getId());
        }
    }

    private MenuProduct createMenuProduct(final Long productId, final Integer quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    private Menu createMenu(final String name,
                            final Long price,
                            final Long menuGroupId,
                            final MenuProduct... menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        if (price != null) {
            menu.setPrice(BigDecimal.valueOf(price));
        }
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(Arrays.asList(menuProducts));
        return menu;
    }
}
