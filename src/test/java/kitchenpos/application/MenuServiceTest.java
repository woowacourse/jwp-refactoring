package kitchenpos.application;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;
    private MenuProduct menuProduct;
    private List<MenuProduct> menuProducts;
    private Product product;
    private Menu menu;
    private List<Menu> menus;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setMenuId(1L);
        menuProduct.setQuantity(1);

        menuProducts = new ArrayList<>();
        menuProducts.add(menuProduct);

        product = new Product();
        product.setPrice(BigDecimal.valueOf(16000.00));
        product.setId(1L);
        product.setName("후라이드치킨");

        menu = new Menu();
        menu.setId(1L);
        menu.setPrice(BigDecimal.valueOf(16000.00));
        menu.setName("후라이드치킨");
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(menuProducts);

        menus = new ArrayList<>();
        menus.add(menu);
    }

    @DisplayName("메뉴 생성")
    @Test
    void create() {
        given(menuGroupDao.existsById(anyLong()))
            .willReturn(true);
        given(productDao.findById(anyLong()))
            .willReturn(Optional.of(product));
        given(menuProductDao.save(menuProduct))
            .willReturn(menuProduct);
        given(menuDao.save(menu))
            .willReturn(menu);

        menuService.create(menu);

        verify(menuDao).save(menu);
    }


    @DisplayName("메뉴 불러오기")
    @Test
    void list() {
        Menu otherMenu = new Menu();
        otherMenu.setId(2L);

        menus.add(otherMenu);

        given(menuDao.findAll())
            .willReturn(menus);
        menuService.list();

        verify(menuDao).findAll();
        verify(menuProductDao, times(2)).findAllByMenuId(anyLong());
    }
}
