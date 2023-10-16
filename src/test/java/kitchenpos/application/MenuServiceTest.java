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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
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
        // given
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

        // when
        menuService.create(menu);

        // then
        then(menuDao).should(times(1)).save(menu);
        then(menuProductDao).should(times(1)).save(menuProduct);
    }

    @Test
    void 메뉴의_가격은_0원_이상이다() {
        // given
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(-1));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuGroupDao).should(never()).existsById(anyLong());
    }

    @Test
    void 주문하려는_메뉴_그룹은_이미_존재하는_그룹이어야_한다() {
        // given
        Menu menu = new Menu();
        menu.setMenuGroupId(1L);
        menu.setPrice(BigDecimal.valueOf(100));

        given(menuGroupDao.existsById(anyLong()))
                .willReturn(false);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        then(productDao).should(never()).findById(anyLong());
    }

    @Test
    void 메뉴의_가격이_구성_상품의_합보다_크면_예외발생() {
        // given
        Menu menu = new Menu();
        menu.setMenuGroupId(1L);
        menu.setPrice(BigDecimal.valueOf(11));

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(1);

        menu.setMenuProducts(List.of(menuProduct));

        Product product = new Product();
        product.setPrice(BigDecimal.TEN);

        given(menuGroupDao.existsById(anyLong()))
                .willReturn(true);

        given(productDao.findById(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuDao).should(never()).save(any());
    }

    @Test
    void 메뉴_구성_상품은_이미_존재하는_상품이어야_한다() {
        // given
        Menu menu = new Menu();
        menu.setMenuGroupId(1L);
        menu.setPrice(BigDecimal.valueOf(9));

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(1);

        menu.setMenuProducts(List.of(menuProduct));

        Product product = new Product();
        product.setPrice(BigDecimal.TEN);

        given(menuGroupDao.existsById(anyLong()))
                .willReturn(true);

        given(productDao.findById(anyLong()))
                .willReturn(Optional.of(product));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuDao).should(never()).save(any());
    }

    @Test
    void 주문하려는_메뉴에_가격이_0원_이하면_예외() {
        // given
        Menu menu = new Menu();
        menu.setMenuGroupId(1L);
        menu.setPrice(BigDecimal.valueOf(100));

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(1);

        menu.setMenuProducts(List.of(menuProduct));

        Product product = new Product();
        product.setPrice(BigDecimal.ZERO);

        given(menuGroupDao.existsById(anyLong()))
                .willReturn(true);

        given(productDao.findById(anyLong()))
                .willReturn(Optional.of(product));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuDao).should(never()).save(any());
    }
}
