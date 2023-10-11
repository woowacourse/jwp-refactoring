package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
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
    class 메뉴_생성_테스트 {

        @Test
        void 메뉴를_정상_생성한다() {
            // given
            Menu menu = new Menu();
            menu.setId(1L);
            menu.setName("name");
            menu.setPrice(new BigDecimal(10_000));
            menu.setMenuGroupId(2L);

            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(3L);
            menuProduct.setQuantity(4L);
            menu.setMenuProducts(List.of(menuProduct));

            Product product = new Product();
            product.setId(3L);
            product.setPrice(new BigDecimal(2_500));

            given(menuGroupDao.existsById(anyLong())).willReturn(true);
            given(productDao.findById(anyLong())).willReturn(Optional.of(product));
            given(menuDao.save(any(Menu.class))).willReturn(menu);
            given(menuProductDao.save(any(MenuProduct.class))).willReturn(menuProduct);

            // when
            Menu savedMenu = menuService.create(menu);

            // then
            SoftAssertions.assertSoftly(softly -> {
                assertThat(savedMenu.getId()).isEqualTo(1L);
                assertThat(savedMenu.getName()).isEqualTo("name");
                assertThat(savedMenu.getPrice()).isEqualTo(new BigDecimal(10_000));
            });
            assertThat(menuService.create(menu)).isInstanceOf(Menu.class);
        }

        @Test
        void 가격이_없는_메뉴를_생성_시_예외를_반환한다() {
            // given
            Menu menu = new Menu();
            menu.setId(1L);
            menu.setName("name");
            menu.setPrice(null);
            menu.setMenuGroupId(2L);

            // when, then
            assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
        }

        @Test
        void 음의_가격을_갖는_메뉴를_생성_시_예외를_반환한다() {
            // given
            Menu menu = new Menu();
            menu.setId(1L);
            menu.setName("name");
            menu.setPrice(new BigDecimal(-1));
            menu.setMenuGroupId(2L);

            // when, then
            assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
        }

        @Test
        void 존재하지_않는_메뉴_그룹에_속한_메뉴를_생성_시_예외를_반환한다() {
            // given
            Menu menu = new Menu();
            menu.setId(1L);
            menu.setName("name");
            menu.setPrice(new BigDecimal(10_000));
            menu.setMenuGroupId(2L);

            given(menuGroupDao.existsById(anyLong())).willReturn(false);

            // when, then
            assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
        }

        @Test
        void 존재하지_않는_상품_id로_메뉴를_생성_시_예외를_반환한다() {
            // given
            Menu menu = new Menu();
            menu.setId(1L);
            menu.setName("name");
            menu.setPrice(new BigDecimal(10_000));
            menu.setMenuGroupId(2L);

            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(-1L);
            menu.setMenuProducts(List.of(menuProduct));

            given(menuGroupDao.existsById(anyLong())).willReturn(true);
            given(productDao.findById(anyLong())).willReturn(Optional.empty());

            // when, then
            assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
        }

        @Test
        void 상품_가격과_수량의_곱이_메뉴_가격과_같지_않은_메뉴를_생성_시_예외를_반환한다() {
            // given
            Menu menu = new Menu();
            menu.setId(1L);
            menu.setName("name");
            menu.setPrice(new BigDecimal(10_000));
            menu.setMenuGroupId(2L);

            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(3L);
            menuProduct.setQuantity(4L);
            menu.setMenuProducts(List.of(menuProduct));

            Product product = new Product();
            product.setId(3L);
            product.setPrice(new BigDecimal(250));

            given(menuGroupDao.existsById(anyLong())).willReturn(true);
            given(productDao.findById(anyLong())).willReturn(Optional.of(product));

            // when, then
            assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
        }
    }

    @Test
    void 메뉴를_전체_조회한다() {
        // given
        Menu menu = new Menu();
        menu.setId(1L);

        MenuProduct menuProduct = new MenuProduct();

        given(menuDao.findAll()).willReturn(List.of(menu));
        given(menuProductDao.findAllByMenuId(anyLong())).willReturn(List.of(menuProduct));

        // when
        List<Menu> menus = menuService.list();

        // then
        SoftAssertions.assertSoftly(softly -> {
            assertThat(menus).isNotEmpty();
            assertThat(menus.get(0).getMenuProducts()).isNotEmpty();
        });
    }
}
