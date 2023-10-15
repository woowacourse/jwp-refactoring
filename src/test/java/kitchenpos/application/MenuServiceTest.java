package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

class MenuServiceTest extends ServiceTest{

    @Autowired
    private MenuService menuService;

    private Menu menu;
    private MenuProduct menuProduct;
    private Product product;

    @BeforeEach
    void setUp() {
        product = makeProduct();
        menuProduct = makeMenuProduct();
        menu = makeMenu(menuProduct);
    }

    @Test
    void 메뉴를_생성한다() {
        Mockito.when(menuGroupDao.existsById(anyLong()))
                .thenReturn(true);

        Mockito.when(productDao.findById(anyLong()))
                .thenReturn(Optional.ofNullable(product));

        Mockito.when(menuDao.save(any(Menu.class)))
                .thenReturn(menu);
        Mockito.when(menuProductDao.save(any(MenuProduct.class)))
                .thenReturn(menuProduct);

        Menu saved = menuService.create(menu);

        assertThat(saved.getId()).isEqualTo(menu.getId());

    }

    @Test
    void 전체_메뉴를_조회한다() {

        Mockito.when(menuDao.findAll())
                .thenReturn(List.of(menu, menu));
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

    private Menu makeMenu(MenuProduct menuProduct) {
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("메뉴");
        menu.setPrice(BigDecimal.ONE);
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(List.of(menuProduct));
        return menu;
    }
}
