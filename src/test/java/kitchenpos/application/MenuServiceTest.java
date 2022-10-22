package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private ProductDao productDao;

    @Mock
    private MenuProductDao menuProductDao;

    @InjectMocks
    private MenuService menuService;

    @Test
    void 메뉴를_생성한다() {
        // given
        // menu product 생성
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(3L);
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct);

        // menu 생성
        BigDecimal price = BigDecimal.valueOf(13000);
        Menu menu = new Menu(1L, "pasta", price, 1L, menuProducts);

        when(menuDao.save(any(Menu.class))).thenReturn(menu);
        when(menuGroupDao.existsById(any(Long.class))).thenReturn(true);

        // product 생성
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(13000));
        when(productDao.findById(any(Long.class))).thenReturn(Optional.of(product));

        when(menuProductDao.save(any(MenuProduct.class))).thenReturn(menuProduct);

        // when
        Menu savedMenu = menuService.create(menu);

        // then
        Assertions.assertAll(
                () -> assertThat(savedMenu.getId()).isNotNull(),
                () -> assertThat(savedMenu.getName()).isEqualTo("pasta"),
                () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(1L)
        );
    }

    @Test
    void price가_null이면_예외를_반환한다() {
        // given
        Menu menu = new Menu();

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void price가_음수면_예외를_반환한다() {
        // given
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(-1000));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void menu_group이_존재하지_않으면_예외를_반환한다() {
        // given
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(13000));
        menu.setMenuGroupId(100L);
        when(menuGroupDao.existsById(100L)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 가격_총합이_0이하이면_예외를_반환한다() {
        // given
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(13000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(new ArrayList<>());
        when(menuGroupDao.existsById(1L)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품이_존재하지_않으면_예외를_반환한다() {
        // given
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(3L);
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct);

        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(13000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(menuProducts);
        when(menuGroupDao.existsById(1L)).thenReturn(true);
        when(productDao.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_목록을_조회한다() {
        // given
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setPrice(BigDecimal.valueOf(13000));
        menu.setMenuGroupId(1L);
        when(menuDao.findAll()).thenReturn(Arrays.asList(menu));

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(3L);
        when(menuProductDao.findAllByMenuId(any(Long.class))).thenReturn(Arrays.asList(menuProduct));

        // when
        List<Menu> menus = menuService.list();

        // then
        Assertions.assertAll(
                () -> assertThat(menus).hasSize(1),
                () -> assertThat(menus.get(0).getMenuProducts()).hasSize(1)
        );
    }
}
