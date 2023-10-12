package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴를 생성, 저장한다.")
    @Test
    void create_success() {
        // given
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("menuGroup");

        final Product product = new Product();
        product.setId(1L);
        product.setName("product");
        product.setPrice(BigDecimal.valueOf(1000L));

        final MenuProduct menuProduct = new MenuProduct();
        final Menu menu = new Menu();
        menu.setId(1L);
        menu.setMenuProducts(List.of(menuProduct));
        menu.setMenuGroupId(menuGroup.getId());
        menu.setName("menu");
        menu.setPrice(BigDecimal.valueOf(2000L));

        menuProduct.setMenuId(menu.getId());
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2L);
        menuProduct.setSeq(1L);

        given(menuGroupDao.existsById(anyLong()))
            .willReturn(true);
        given(productDao.findById(anyLong()))
            .willReturn(Optional.of(product));
        given(menuDao.save(any(Menu.class)))
            .willReturn(menu);
        given(menuProductDao.save(any(MenuProduct.class)))
            .willReturn(menuProduct);

        // when
        final Menu savedMenu = menuService.create(menu);

        // then
        assertThat(savedMenu).isEqualTo(menu);
    }

    @DisplayName("메뉴를 저장 시, 가격이 0보다 작으면 예외가 발생한다.")
    @Test
    void create_price_fail() {
        // given
        final MenuProduct menuProduct = new MenuProduct();
        final Menu menu = new Menu();
        menu.setId(1L);
        menu.setMenuProducts(List.of(menuProduct));
        menu.setMenuGroupId(1L);
        menu.setName("menu");
        menu.setPrice(BigDecimal.valueOf(-1L));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 저장 시, 메뉴 그룹이 존재하지 않는다면 예외가 발생한다.")
    @Test
    void create_menuGroup_fail() {
        // given
        final MenuProduct menuProduct = new MenuProduct();
        final Menu menu = new Menu();
        menu.setId(1L);
        menu.setMenuProducts(List.of(menuProduct));
        menu.setMenuGroupId(1L);
        menu.setName("menu");
        menu.setPrice(BigDecimal.valueOf(1000L));

        given(menuGroupDao.existsById(anyLong()))
            .willReturn(false);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 저장 시, 메뉴의 상품이 존재하지 않는다면 예외가 발생한다.")
    @Test
    void create_menuProduct_fail() {
        // given
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        final Menu menu = new Menu();
        menu.setId(1L);
        menu.setMenuProducts(List.of(menuProduct));
        menu.setMenuGroupId(1L);
        menu.setName("menu");
        menu.setPrice(BigDecimal.valueOf(1000L));

        given(menuGroupDao.existsById(anyLong()))
            .willReturn(true);
        given(productDao.findById(anyLong()))
            .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 저장 시, 메뉴의 총 가격이 상품 총 가격보다 크다면 예외가 발생한다.")
    @Test
    void create_menuTotalPrice_fail() {
        // given
        final Product product = new Product();
        product.setId(1L);
        product.setName("product");
        product.setPrice(BigDecimal.valueOf(1000L));

        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2L);

        final Menu menu = new Menu();
        menu.setId(1L);
        menu.setMenuProducts(List.of(menuProduct));
        menu.setMenuGroupId(1L);
        menu.setName("menu");
        menu.setPrice(BigDecimal.valueOf(2001L));

        given(menuGroupDao.existsById(anyLong()))
            .willReturn(true);
        given(productDao.findById(anyLong()))
            .willReturn(Optional.of(product));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 메뉴를 조회한다.")
    @Test
    void list() {
        // given
        final MenuProduct menuProduct = new MenuProduct();
        final Menu menu1 = new Menu();
        menu1.setId(1L);
        menu1.setMenuProducts(List.of(menuProduct));
        menu1.setMenuGroupId(1L);
        menu1.setName("menu1");
        menu1.setPrice(BigDecimal.valueOf(2000L));

        menuProduct.setMenuId(menu1.getId());
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2L);
        menuProduct.setSeq(1L);

        given(menuDao.findAll())
            .willReturn(List.of(menu1));

        // when
        final List<Menu> foundMenus = menuService.list();

        // then
        assertThat(foundMenus).containsExactly(menu1);
    }
}
