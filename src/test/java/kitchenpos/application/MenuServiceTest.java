package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
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
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    void 메뉴를_생성한다() {
        // given
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.TEN);
        menu.setMenuGroupId(1L);

        Product product = new Product("치킨", BigDecimal.TEN);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(1L);
        List<MenuProduct> menuProducts = List.of(menuProduct);
        menu.setMenuProducts(menuProducts);

        given(menuGroupDao.existsById(1L))
                .willReturn(true);
        given(productRepository.findById(1L))
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
    void 메뉴의_가격이_null_이면_예외발생() {
        // given
        Menu menu = new Menu();

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuGroupDao).should(never()).existsById(anyLong());
    }

    @Test
    void 생성하려는_메뉴의_메뉴그룹이_존재하지_않으면_예외발생() {
        // given
        Menu menu = new Menu();
        menu.setMenuGroupId(1L);
        menu.setPrice(BigDecimal.valueOf(100));

        given(menuGroupDao.existsById(anyLong()))
                .willReturn(false);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        then(productRepository).should(never()).findById(anyLong());
    }

    @Test
    void 생성하려는_메뉴의_구성_상품_중_가격이_음수인_상품이_있으면_예외발생() {
        // given
        Menu menu = new Menu();
        menu.setMenuGroupId(1L);
        menu.setPrice(BigDecimal.valueOf(-1));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuGroupDao).should(never()).existsById(anyLong());
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

        Product product = new Product("치킨", BigDecimal.TEN);

        given(menuGroupDao.existsById(anyLong()))
                .willReturn(true);

        given(productRepository.findById(anyLong()))
                .willReturn(Optional.of(product));

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

        Product product = new Product("치킨", BigDecimal.TEN);

        given(menuGroupDao.existsById(anyLong()))
                .willReturn(true);

        // 예외 상황: 존재하지 않는 상품
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.empty());

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

        Product product = new Product("치킨", BigDecimal.ZERO);

        given(menuGroupDao.existsById(anyLong()))
                .willReturn(true);

        given(productRepository.findById(anyLong()))
                .willReturn(Optional.of(product));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuDao).should(never()).save(any());
    }
}
