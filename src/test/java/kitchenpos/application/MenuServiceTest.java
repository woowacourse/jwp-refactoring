package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴")
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

    @DisplayName("메뉴를 등록할 수 있다")
    @Test
    void create() {
        final Menu menu = new Menu();
        menu.setName("분짜와 스프링롤");
        menu.setPrice(BigDecimal.valueOf(16000));
        menu.setMenuGroupId(1L);
        final Menu savedMenu = new Menu();
        savedMenu.setId(1L);
        savedMenu.setName("분짜와 스프링롤");
        savedMenu.setPrice(BigDecimal.valueOf(16000));
        savedMenu.setMenuGroupId(1L);
        final MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setSeq(1L);
        menuProduct1.setProductId(1L);
        menuProduct1.setQuantity(1);
        final Product product1 = Product.builder()
                .name("분짜")
                .price(BigDecimal.valueOf(13000))
                .id(1L)
                .build();
        final MenuProduct menuProduct2 = new MenuProduct();
        menuProduct2.setSeq(2L);
        menuProduct2.setProductId(2L);
        menuProduct2.setQuantity(1);
        final Product product2 = Product.builder()
                .name("스프링롤")
                .price(BigDecimal.valueOf(3000))
                .id(2L)
                .build();
        menu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));

        when(menuGroupDao.existsById(1L)).thenReturn(true);
        when(productDao.findById(1L)).thenReturn(Optional.of(product1));
        when(productDao.findById(2L)).thenReturn(Optional.of(product2));
        when(menuDao.save(menu)).thenReturn(savedMenu);
        when(menuProductDao.save(menuProduct1)).thenReturn(menuProduct1);
        when(menuProductDao.save(menuProduct2)).thenReturn(menuProduct2);

        final Menu actual = menuService.create(menu);
        assertThat(actual).isEqualTo(savedMenu);
    }

    @DisplayName("메뉴의 가격은 0 원 이상이어야 한다")
    @Test
    void createExceptionPriceUnderZero() {
        final Menu menu = new Menu();
        menu.setName("분짜와 스프링롤");
        menu.setPrice(BigDecimal.valueOf(-1));
        menu.setMenuGroupId(1L);

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴가 속한 메뉴 그룹이 존재해야 한다")
    @Test
    void createExceptionMenuGroup() {
        final Menu menu = new Menu();
        menu.setName("분짜와 스프링롤");
        menu.setPrice(BigDecimal.valueOf(16000));
        menu.setMenuGroupId(1L);

        when(menuGroupDao.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 상품 목록에서 (상품의 가격 * 메뉴 상품의 갯수) 의 합이 0 원 이상이어야 한다")
    @Test
    void createExceptionSum() {
        final Menu menu = new Menu();
        menu.setName("분짜와 스프링롤");
        menu.setPrice(BigDecimal.valueOf(16000));
        menu.setMenuGroupId(1L);
        final MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setSeq(1L);
        menuProduct1.setProductId(1L);
        menuProduct1.setQuantity(1);
        final Product product1 = Product.builder()
                .name("분짜")
                .price(BigDecimal.valueOf(13000))
                .id(1L).build();
        final MenuProduct menuProduct2 = new MenuProduct();
        menuProduct2.setSeq(2L);
        menuProduct2.setProductId(2L);
        menuProduct2.setQuantity(1);
        final Product product2 = Product.builder()
                .name("스프링롤")
                .price(BigDecimal.valueOf(2000))
                .id(2L).build();
        menu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));

        when(menuGroupDao.existsById(1L)).thenReturn(true);
        when(productDao.findById(1L)).thenReturn(Optional.of(product1));
        when(productDao.findById(2L)).thenReturn(Optional.of(product2));

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 목록을 조회할 수 있다")
    @Test
    void list() {
        final Menu menu1 = new Menu();
        menu1.setId(1L);
        final Menu menu2 = new Menu();
        menu2.setId(2L);
        final List<Menu> menus = Arrays.asList(menu1, menu2);
        final MenuProduct menuProduct1 = new MenuProduct();
        final MenuProduct menuProduct2 = new MenuProduct();
        final List<MenuProduct> menuProducts1 = Arrays.asList(menuProduct1, menuProduct2);
        final MenuProduct menuProduct3 = new MenuProduct();
        final List<MenuProduct> menuProducts2 = Collections.singletonList(menuProduct3);

        when(menuDao.findAll()).thenReturn(menus);
        when(menuProductDao.findAllByMenuId(1L)).thenReturn(menuProducts1);
        when(menuProductDao.findAllByMenuId(2L)).thenReturn(menuProducts2);

        final List<Menu> actual = menuService.list();
        assertThat(actual).isEqualTo(menus);
    }
}
