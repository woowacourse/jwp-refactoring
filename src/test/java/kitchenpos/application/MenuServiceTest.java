package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    @Nested
    class 메뉴_생성 {

        @Nested
        class 메뉴_가격_정책 {

            @Test
            void 메뉴_생성_시_가격이_음수라면_예외() {
                // given
                Menu menu = MenuFixture.builder()
                    .withPrice(-1000L)
                    .build();

                // when && then
                assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void 메뉴_생성_시_가격을_보내지_않으면_예외() {
                // given
                Menu menu = MenuFixture.builder()
                    .build();

                // when && then
                assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void 메뉴_상품_가격의_총합이_메뉴의_가격보다_높으면_예외() {
                // given
                MenuCreateHelper menuCreateHelper = new MenuCreateHelper(1L, "menu", 1L);
                menuCreateHelper.withMenuProduct(1L, "상품1", 1000L, 5L);
                menuCreateHelper.withMenuProduct(1L, "상품2", 100L, 3L);
                menuCreateHelper.price(5400L);

                given(menuGroupDao.existsById(anyLong()))
                    .willReturn(true);
                given(productDao.findById(anyLong()))
                    .willReturn(
                        Optional.of(menuCreateHelper.products.get(0)),
                        Optional.of(menuCreateHelper.products.get(1))
                    );

                // when && then
                assertThatThrownBy(() -> menuService.create(menuCreateHelper.menu))
                    .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Test
        void 메뉴_그룹이_저장된_메뉴_그룹이_아니면_예외() {
            // given
            long nonExistentMenuGroupId = 1L;
            Menu menu = MenuFixture.builder()
                .withPrice(1000L)
                .withMenuGroupId(nonExistentMenuGroupId)
                .build();

            given(menuGroupDao.existsById(nonExistentMenuGroupId))
                .willReturn(false);

            // when && then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_상품을_메뉴_상품에_넣으면_예외() {
            // given
            Menu menu = MenuFixture.builder()
                .withPrice(1000L)
                .withMenuGroupId(1L)
                .withMenuProducts(
                    List.of(
                        MenuProductFixture.builder()
                            .withProductId(1L)
                            .build())
                )
                .build();

            given(menuGroupDao.existsById(anyLong()))
                .willReturn(true);
            given(productDao.findById(anyLong()))
                .willReturn(Optional.empty());

            // when && then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 생성_완료() {
            // given
            long menuGroupId = 1L;
            long menuId = 1L;
            MenuCreateHelper menuCreateHelper = new MenuCreateHelper(menuId, "menu", menuGroupId);
            menuCreateHelper.withMenuProduct(1L, "상품1", 1000L, 5L);
            menuCreateHelper.withMenuProduct(1L, "상품2", 100L, 3L);
            menuCreateHelper.price(5300L);

            given(menuGroupDao.existsById(anyLong()))
                .willReturn(true);
            List<Product> products = menuCreateHelper.products;
            given(productDao.findById(anyLong()))
                .willReturn(
                    Optional.of(products.get(0)),
                    Optional.of(products.get(1))
                );
            Menu menu = menuCreateHelper.menu;
            List<MenuProduct> menuProducts = menu.getMenuProducts();
            given(menuDao.save(any()))
                .willReturn(menu);
            given(menuProductDao.save(any()))
                .willReturn(menuProducts.get(0), menuProducts.get(1));

            // when
            Menu actual = menuService.create(menu);

            // then
            assertSoftly(softAssertions -> {
                assertThat(actual.getMenuGroupId()).isEqualTo(menuGroupId);
                assertThat(actual.getMenuProducts()).hasSize(2);
                assertThat(actual.getMenuProducts())
                    .allMatch(menuProduct -> menuProduct.getMenuId().equals(menuId));
            });
        }

        private class MenuCreateHelper {

            private Menu menu;
            private List<Product> products = new ArrayList<>();

            public MenuCreateHelper(Long menuId, String name, Long menuGroupId) {
                this.menu = new Menu(menuId, name, null, menuGroupId, new ArrayList<>());
            }

            public MenuProduct withMenuProduct(Long productId, String name, Long price, Long quantity) {
                Product product = new Product(productId, name, BigDecimal.valueOf(price));
                products.add(product);
                List<MenuProduct> menuProducts = menu.getMenuProducts();
                MenuProduct newMenuProduct = new MenuProduct(menuProducts.size() + 1L, menu.getId(), productId, quantity);
                menuProducts.add(newMenuProduct);
                menu.setMenuProducts(menuProducts);
                return newMenuProduct;
            }

            public Menu price(Long price){
                this.menu.setPrice(BigDecimal.valueOf(price));
                return menu;
            }
        }
    }

    @Test
    void 메뉴_목록_검색() {
        // given
        MenuProduct firstMenuProduct = MenuProductFixture.builder().build();
        MenuProduct secondMenuProduct = MenuProductFixture.builder().build();

        List<MenuProduct> ofFirstMenu = List.of(firstMenuProduct, secondMenuProduct);
        Menu menu = MenuFixture.builder()
            .withId(1L)
            .withMenuProducts(ofFirstMenu)
            .build();

        given(menuDao.findAll())
            .willReturn(List.of(menu));
        given(menuProductDao.findAllByMenuId(anyLong()))
            .willReturn(ofFirstMenu);

        // when
        List<Menu> actual = menuService.list();

        // then
        assertThat(actual).isEqualTo(List.of(menu));
    }
}
