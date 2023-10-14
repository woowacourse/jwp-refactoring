package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @MockBean
    private MenuDao menuDao;
    @MockBean
    private MenuGroupDao menuGroupDao;
    @MockBean
    private MenuProductDao menuProductDao;
    @MockBean
    private ProductDao productDao;

    @Test
    void 메뉴를_생성한다() {
        Mockito.when(menuGroupDao.existsById(anyLong()))
                .thenReturn(true);

        Product product = makeProduct();
        Mockito.when(productDao.findById(anyLong()))
                .thenReturn(Optional.ofNullable(product));

        MenuProduct menuProduct = makeMenuProduct();
        Menu menu = makeMenu(menuProduct);
        Mockito.when(menuDao.save(any(Menu.class)))
                .thenReturn(menu);
        Mockito.when(menuProductDao.save(any(MenuProduct.class)))
                .thenReturn(menuProduct);

        Menu saved = menuService.create(menu);

        assertThat(saved.getId()).isEqualTo(menu.getId());

    }

    private Menu makeMenu(MenuProduct menuProduct) {
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("메뉴");
        menu.setPrice(BigDecimal.ONE);
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(List.of(menuProduct));
        return menu;
    }

    @Test
    void 전체_메뉴를_조회한다() {
        MenuProduct menuProduct = makeMenuProduct();
        Menu menu = makeMenu(menuProduct);

        Mockito.when(menuDao.findAll())
                .thenReturn(List.of(menu,menu));
        Mockito.when(menuProductDao.findAllByMenuId(anyLong()))
                .thenReturn(List.of(menuProduct));

        List<Menu> menus = menuService.list();
        assertThat(menus.size()).isEqualTo(2);
    }

    private MenuProduct makeMenuProduct() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(1L);
        return menuProduct;
    }

    private Product makeProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("상품");
        product.setPrice(BigDecimal.ONE);
        return product;
    }

}
