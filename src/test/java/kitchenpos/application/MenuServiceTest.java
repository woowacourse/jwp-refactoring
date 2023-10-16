package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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

    @Test
    void 메뉴를_생성한다() {
        // given
        final Menu savedMenu = new Menu();
        savedMenu.setId(1L);

        final Product savedProduct = new Product();
        savedProduct.setPrice(new BigDecimal(1000));

        final MenuProduct savedMenuProduct = new MenuProduct();

        when(menuGroupDao.existsById(anyLong()))
                .thenReturn(true);
        when(productDao.findById(anyLong()))
                .thenReturn(Optional.of(savedProduct));
        when(menuDao.save(any(Menu.class)))
                .thenReturn(savedMenu);
        when(menuProductDao.save(any(MenuProduct.class)))
                .thenReturn(savedMenuProduct);

        // when
        final Menu menu = new Menu();
        menu.setMenuGroupId(1L);
        menu.setPrice(new BigDecimal(1000));

        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(1);
        menu.setMenuProducts(List.of(menuProduct));

        final Menu result = menuService.create(menu);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(1),
                () -> assertThat(result.getMenuProducts()).hasSize(1)
        );
    }

    @Test
    void 메뉴를_생성할_때_가격이_null이면_실패한다() {
        // given, when, then
        assertThatThrownBy(() -> menuService.create(new Menu()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴를_생성할_때_가격이_0보다_작으면_실패한다() {
        // given
        final Menu menu = new Menu();
        menu.setPrice(new BigDecimal(-1000));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴를_생성할_때_존재하지_않는_메뉴_그룹_아이디를_전달하면_실패한다() {
        // given
        final Menu menu = new Menu();
        menu.setPrice(new BigDecimal(1000));
        menu.setMenuGroupId(1L);

        when(menuGroupDao.existsById(anyLong()))
                .thenReturn(false);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴를_생성할_때_존재하지_않는_상품_아이디를_전달하면_실패한다() {
        // given
        when(menuGroupDao.existsById(anyLong()))
                .thenReturn(true);
        when(productDao.findById(anyLong()))
                .thenReturn(Optional.empty());

        // when
        final Menu menu = new Menu();
        menu.setPrice(new BigDecimal(1000));
        menu.setMenuGroupId(1L);

        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menu.setMenuProducts(List.of(menuProduct));

        // then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴를_생성할_때_전달한_가격과_메뉴_상품들의_가격의_합이_일치하지_않으면_실패한다() {
        // given
        final Product savedProduct = new Product();
        savedProduct.setPrice(new BigDecimal(500));

        when(menuGroupDao.existsById(anyLong()))
                .thenReturn(true);
        when(productDao.findById(anyLong()))
                .thenReturn(Optional.of(savedProduct));

        // when
        final Menu menu = new Menu();
        menu.setPrice(new BigDecimal(1000));
        menu.setMenuGroupId(1L);

        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(1);
        menu.setMenuProducts(List.of(menuProduct));

        // then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 전체_메뉴_목록을_반환한다() {
        // given
        when(menuDao.findAll())
                .thenReturn(Collections.emptyList());

        // when
        final List<Menu> result = menuService.list();

        // then
        assertThat(result).isEmpty();
    }
}