package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

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
    private MenuService service;

    private Product product1;
    private Product product2;
    private MenuProduct menuProduct1;
    private MenuProduct menuProduct2;
    private MenuGroup menuGroup;
    private Menu menu;

    @BeforeEach
    public void setUp() {
        product1 = new Product();
        product1.setId(5L);
        product1.setName("포테이노 피자");
        product1.setPrice(BigDecimal.valueOf(12000L));

        menuProduct1 = new MenuProduct();
        menuProduct1.setMenuId(10L);
        menuProduct1.setProductId(5L);
        menuProduct1.setQuantity(1);
        menuProduct1.setSeq(1L);

        product2 = new Product();
        product2.setId(6L);
        product2.setName("콜라");
        product2.setPrice(BigDecimal.valueOf(2000L));

        menuProduct2 = new MenuProduct();
        menuProduct2.setMenuId(10L);
        menuProduct2.setProductId(6L);
        menuProduct2.setQuantity(1);
        menuProduct2.setSeq(2L);

        menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("패스트 푸드");

        menu = new Menu();
        menu.setId(10L);
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Lists.newArrayList(menuProduct1, menuProduct2));
        menu.setName("포테이토 피자 세트");
        menu.setPrice(BigDecimal.valueOf(13000L));
    }

    @DisplayName("메뉴 생성 실패 - 가격이 null일 때")
    @Test
    public void createFailPriceNull() {
        menu.setPrice(null);

        assertThatThrownBy(() -> service.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 실패 - 가격이 음수일 때")
    @Test
    public void createFailPriceNegative() {
        menu.setPrice(BigDecimal.valueOf(-1L));

        assertThatThrownBy(() -> service.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 실패 - 존재 하지 않는 메뉴 그룹을 참조할 때")
    @Test
    public void createFailNotExistedMenuGroup() {
        given(menuGroupDao.existsById(anyLong())).willReturn(false);

        assertThatThrownBy(() -> service.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 실패 - 가격이 products 가격 합보다 클 때")
    @Test
    public void createFailPriceOverSumOfProductsPrice() {
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(eq(5L))).willReturn(Optional.of(product1));
        given(productDao.findById(eq(6L))).willReturn(Optional.of(product2));
        menu.setPrice(BigDecimal.valueOf(15000L));

        assertThatThrownBy(() -> service.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 성공")
    @Test
    public void createMenu() {
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(eq(5L))).willReturn(Optional.of(product1));
        given(productDao.findById(eq(6L))).willReturn(Optional.of(product2));
        given(menuDao.save(any(Menu.class))).willReturn(menu);

        final Menu savedMenu = service.create(this.menu);

        assertThat(savedMenu.getId()).isEqualTo(10L);
        assertThat(savedMenu.getMenuGroupId()).isEqualTo(1L);
        assertThat(savedMenu.getName()).isEqualTo("포테이토 피자 세트");
        assertThat(savedMenu.getPrice()).isEqualTo(BigDecimal.valueOf(13000L));
        assertThat(savedMenu.getMenuProducts()).hasSize(2);
    }

    @DisplayName("메뉴 목록 조회")
    @Test
    public void reaeMenus() {
        given(menuDao.findAll()).willReturn(Lists.newArrayList(menu));
        given(menuProductDao.findAllByMenuId(anyLong()))
            .willReturn(Lists.newArrayList(menuProduct1, menuProduct2));

        final List<Menu> menus = service.list();

        assertThat(menus).hasSize(1);
        assertThat(menus).contains(menu);
    }
}
