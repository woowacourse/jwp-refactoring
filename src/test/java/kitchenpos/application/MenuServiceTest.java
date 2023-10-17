package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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
    void 메뉴_가격은_NULL_일_수_없다() {
        Menu menu = new Menu();
        menu.setPrice(null);

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_가격은_음수일_수_없다() {
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(-1));

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_그룹_ID_가_존재하지_않은_경우_예외가_발생한다() {
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(1000));
        menu.setMenuGroupId(1L);

        when(menuGroupDao.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 제품_ID가_존재하지_않는_경우_예외가_발생한다() {
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(1000));
        menu.setMenuGroupId(1L);
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(2L);
        menu.setMenuProducts(List.of(menuProduct));

        when(menuGroupDao.existsById(1L)).thenReturn(true);
        when(productDao.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 전체_제품의_총_가격은_메뉴_가격과_동일하다() {
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(1000));
        menu.setMenuGroupId(1L);
        MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setProductId(1L);
        MenuProduct menuProduct2 = new MenuProduct();
        menuProduct2.setProductId(2L);
        menu.setMenuProducts(List.of(menuProduct1, menuProduct2));

        when(menuGroupDao.existsById(1L)).thenReturn(true);

        Product 로제떡볶이 = ProductFixtures.로제떡볶이();
        when(productDao.findById(1L)).thenReturn(Optional.of(로제떡볶이));

        Product 짜장떡볶이 = ProductFixtures.짜장떡볶이();
        when(productDao.findById(2L)).thenReturn(Optional.of(짜장떡볶이));

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_생성를_생성할_수_있다() {
        Menu menu = new Menu();
        menu.setId(0L);
        menu.setPrice(BigDecimal.valueOf(1000));
        menu.setMenuGroupId(1L);
        MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setProductId(3L);
        menuProduct1.setQuantity(1);
        MenuProduct menuProduct2 = new MenuProduct();
        menuProduct2.setProductId(2L);
        menuProduct2.setQuantity(1);
        menu.setMenuProducts(List.of(menuProduct1, menuProduct2));

        when(menuGroupDao.existsById(1L)).thenReturn(true);

        Product 마라떡볶이 = ProductFixtures.마라떡볶이();
        when(productDao.findById(3L)).thenReturn(Optional.of(마라떡볶이));

        Product 짜장떡볶이 = ProductFixtures.짜장떡볶이();
        when(productDao.findById(2L)).thenReturn(Optional.of(짜장떡볶이));

        when(menuDao.save(menu)).thenReturn(menu);
        when(menuProductDao.save(menuProduct1)).thenReturn(menuProduct1);

        menuService.create(menu);

        verify(menuDao).save(menu);
        verify(menuProductDao, times(2)).save(any(MenuProduct.class));

    }

    @Test
    void 전체_메뉴_조회할_수_있다() {
        Menu menu = new Menu();
        menu.setId(2L);
        when(menuDao.findAll()).thenReturn(List.of(menu));

        menuService.list();

        verify(menuDao).findAll();
        verify(menuProductDao).findAllByMenuId(menu.getId());
    }
}
