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
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

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
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.TEN);
        menu.setMenuGroupId(1L);

        Product product = new Product();
        product.setId(1L);
        product.setPrice(BigDecimal.TEN);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(1L);
        List<MenuProduct> menuProducts = List.of(menuProduct);
        menu.setMenuProducts(menuProducts);

        given(menuGroupDao.existsById(1L))
                .willReturn(true);
        given(productDao.findById(1L))
                .willReturn(Optional.of(product));
        given(menuDao.save(menu))
                .willReturn(menu);

        menuService.create(menu);

        then(menuDao).should(times(1)).save(menu);
        then(menuProductDao).should(times(1)).save(menuProduct);
    }

    @Test
    void 메뉴의_가격은_0원_이상이다() {
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(-1));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문하려는_메뉴_그룹은_이미_존재하는_그룹이어야_한다() {
        Menu menu = new Menu();
        menu.setMenuGroupId(1L);
        menu.setPrice(BigDecimal.valueOf(100));

        given(menuGroupDao.existsById(BDDMockito.anyLong()))
                .willReturn(false);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문하려는_메뉴에_존재하는_상품은_이미_존재하는_상품이어야_한다() {
        Menu menu = new Menu();
        menu.setMenuGroupId(1L);
        menu.setPrice(BigDecimal.valueOf(100));

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);

        menu.setMenuProducts(List.of(menuProduct));

        Product product = new Product();
        product.setPrice(BigDecimal.TEN);

        given(menuGroupDao.existsById(BDDMockito.anyLong()))
                .willReturn(true);

        given(productDao.findById(BDDMockito.anyLong()))
                .willReturn(Optional.of(product));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문하려는_메뉴에_가격이_0원_이하면_예외() {
        Menu menu = new Menu();
        menu.setMenuGroupId(1L);
        menu.setPrice(BigDecimal.valueOf(100));

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(1);

        menu.setMenuProducts(List.of(menuProduct));

        Product product = new Product();
        product.setPrice(BigDecimal.ZERO);

        given(menuGroupDao.existsById(BDDMockito.anyLong()))
                .willReturn(true);

        given(productDao.findById(BDDMockito.anyLong()))
                .willReturn(Optional.of(product));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
