package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
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
    private MenuService menuService;

    private MenuProduct menuProduct;
    private Product product;
    private Menu menu;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("강정치킨");
        product.setPrice(BigDecimal.valueOf(17000));

        menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2L);

        menu = new Menu();
        menu.setName("후라이드+후라이드");
        menu.setPrice(BigDecimal.valueOf(19000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Collections.singletonList(menuProduct));
    }

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void create() {
        // given
        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
        given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.of(product));

        Menu expected = 메뉴를_저장한다("후라이드+후라이드", BigDecimal.valueOf(19000), 1L, Collections.singletonList(menuProduct));
        given(menuDao.save(menu)).willReturn(expected);

        // when
        Menu savedMenu = menuService.create(menu);

        // then
        assertThat(savedMenu).isEqualTo(expected);
    }

    @DisplayName("메뉴를 조회할 수 있다.")
    @Test
    void list() {
        // given
        Menu savedMenu = 메뉴를_저장한다(menu.getName(), menu.getPrice(), menu.getMenuGroupId(), menu.getMenuProducts());

        given(menuDao.findAll()).willReturn(Collections.singletonList(savedMenu));
        given(menuProductDao.findAllByMenuId(savedMenu.getId())).willReturn(Collections.singletonList(menuProduct));

        // when
        List<Menu> findMenus = menuService.list();

        // then
        assertThat(findMenus).isEqualTo(Collections.singletonList(savedMenu));
    }

    @DisplayName("메뉴 가격은 0원 이상이어야한다.")
    @Test
    void validateMenuNameLength() {
        // when
        menu.setPrice(BigDecimal.valueOf(-1));

        // then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 제품 번호를 등록할 수 있다.")
    @Test
    void validateMenuProduct() {
        // when
        menu.getMenuProducts().get(0).setQuantity(-1);

        // then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    private Menu 메뉴를_저장한다(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu savedMenu = new Menu();
        savedMenu.setId(1L);
        savedMenu.setName(name);
        savedMenu.setPrice(price);
        savedMenu.setMenuGroupId(menuGroupId);
        savedMenu.setMenuProducts(menuProducts);
        return savedMenu;
    }
}