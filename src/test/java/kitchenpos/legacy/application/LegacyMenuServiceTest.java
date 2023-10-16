package kitchenpos.legacy.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.legacy.dao.MenuDao;
import kitchenpos.legacy.dao.MenuGroupDao;
import kitchenpos.legacy.dao.MenuProductDao;
import kitchenpos.legacy.dao.ProductDao;
import kitchenpos.legacy.domain.Menu;
import kitchenpos.legacy.domain.MenuProduct;
import kitchenpos.legacy.domain.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class LegacyMenuServiceTest {

    @InjectMocks
    LegacyMenuService menuService;

    @Mock
    MenuDao menuDao;

    @Mock
    MenuGroupDao menuGroupDao;

    @Mock
    MenuProductDao menuProductDao;

    @Mock
    ProductDao productDao;

    @Nested
    class create {

        @Test
        void 가격이_null_이면_예외() {
            // given
            Menu menu = new Menu();

            // when
            menu.setPrice(null);

            // then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1})
        void 가격이_0보다_작으면_예외(int price) {
            // given
            Menu menu = new Menu();

            // when
            menu.setPrice(BigDecimal.valueOf(price));

            // then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_그룹_식별자에_대한_메뉴_그룹이_없으면_예외() {
            // given
            Menu menu = new Menu();
            menu.setPrice(BigDecimal.valueOf(1000));
            menu.setMenuGroupId(1L);

            // when
            given(menuGroupDao.existsById(anyLong()))
                .willReturn(false);

            // then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 상품_식별자에_대한_상품이_없으면_예외() {
            // given
            Menu menu = new Menu();
            menu.setPrice(BigDecimal.valueOf(1000));
            menu.setMenuGroupId(1L);
            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(1L);
            menu.setMenuProducts(List.of(menuProduct));
            given(menuGroupDao.existsById(anyLong()))
                .willReturn(true);

            // when
            given(productDao.findById(anyLong()))
                .willReturn(Optional.empty());

            // then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 총합_가격이_메뉴_가격보다_작으면_예외() {
            // given
            Menu menu = new Menu();
            menu.setPrice(BigDecimal.valueOf(1000));
            menu.setMenuGroupId(1L);

            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(1L);
            menuProduct.setQuantity(1);
            menu.setMenuProducts(List.of(menuProduct));

            Product product = new Product();
            product.setId(1L);

            given(menuGroupDao.existsById(anyLong()))
                .willReturn(true);
            given(productDao.findById(1L))
                .willReturn(Optional.of(product));

            // when
            product.setPrice(BigDecimal.valueOf(999));

            // then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        // TODO 해당 케이스를 예외로 고려할 것
        @Test
        void 총합_가격이_메뉴_가격보다_크면_예외를_던지지_않는다() {
            // given
            Menu menu = new Menu();
            menu.setPrice(BigDecimal.valueOf(1000));
            menu.setMenuGroupId(1L);

            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(1L);
            menuProduct.setQuantity(1);
            menu.setMenuProducts(List.of(menuProduct));

            Product product = new Product();
            product.setId(1L);

            given(menuGroupDao.existsById(anyLong()))
                .willReturn(true);
            given(productDao.findById(1L))
                .willReturn(Optional.of(product));
            Menu savedMenu = new Menu();
            savedMenu.setId(1L);
            given(menuDao.save(any(Menu.class)))
                .willReturn(savedMenu);

            // when
            product.setPrice(BigDecimal.valueOf(1001));

            // then
            assertThatNoException()
                .isThrownBy(() -> menuService.create(menu));
        }

        @Test
        void 성공() {
            // given
            Menu menu = new Menu();
            menu.setPrice(BigDecimal.valueOf(1000));
            menu.setMenuGroupId(1L);

            MenuProduct menuProduct1 = new MenuProduct();
            menuProduct1.setProductId(1L);
            menuProduct1.setQuantity(4);
            MenuProduct menuProduct2 = new MenuProduct();
            menuProduct2.setProductId(2L);
            menuProduct2.setQuantity(2);
            menu.setMenuProducts(List.of(menuProduct1, menuProduct2));

            Product product1 = new Product();
            product1.setId(1L);
            product1.setPrice(BigDecimal.valueOf(200));
            Product product2 = new Product();
            product2.setId(2L);
            product2.setPrice(BigDecimal.valueOf(100));

            given(menuGroupDao.existsById(anyLong()))
                .willReturn(true);
            given(productDao.findById(1L))
                .willReturn(Optional.of(product1));
            given(productDao.findById(2L))
                .willReturn(Optional.of(product2));
            Menu savedMenu = new Menu();
            savedMenu.setId(1L);
            given(menuDao.save(any(Menu.class)))
                .willReturn(savedMenu);
            given(menuProductDao.save(any(MenuProduct.class)))
                .willReturn(menuProduct1, menuProduct2);

            // when
            Menu actual = menuService.create(menu);

            // then
            List<MenuProduct> actualMenuProducts = actual.getMenuProducts();
            assertThat(actualMenuProducts)
                .map(MenuProduct::getMenuId)
                .allMatch(menuId -> menuId.equals(actual.getId()));
        }
    }

    @Nested
    class findAll {

        @Test
        void 성공() {
            // given
            Menu menu1 = new Menu();
            menu1.setId(1L);
            Menu menu2 = new Menu();
            menu2.setId(2L);
            given(menuDao.findAll())
                .willReturn(List.of(menu1, menu2));
            given(menuProductDao.findAllByMenuId(1L))
                .willReturn(List.of(new MenuProduct()));
            given(menuProductDao.findAllByMenuId(2L))
                .willReturn(List.of(new MenuProduct(), new MenuProduct()));

            // when
            List<Menu> menus = menuService.findAll();

            // then
            assertThat(menus).hasSize(2);
        }
    }
}
