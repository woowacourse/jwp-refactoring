package kitchenpos.application;

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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
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
    @DisplayName("메뉴 생성 테스트")
    public void createMenuTest() {
        // Given
        final Menu menu = new Menu(1L, "후라이드+후라이드", BigDecimal.valueOf(10000), 1L, null);
        final Product product = new Product(1L, "후라이드", BigDecimal.valueOf(16000));
        final MenuProduct menuProduct = new MenuProduct(1L, 1L, 1L, 1);
        menu.setMenuProducts(List.of(menuProduct));

        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(anyLong())).willReturn(Optional.of(product));
        given(menuDao.save(any(Menu.class))).willReturn(menu);
        given(menuProductDao.save(any(MenuProduct.class))).willReturn(menuProduct);

        // When
        menuService.create(menu);

        // Then
        verify(menuDao).save(any(Menu.class));
        verify(menuProductDao).save(any(MenuProduct.class));
        assertThat(menu.getPrice()).isEqualTo(BigDecimal.valueOf(10000));
    }

    @Test
    @DisplayName("메뉴 목록 조회 테스트")
    public void listMenusTest() {
        // Given
        given(menuDao.findAll()).willReturn(List.of(new Menu()));

        // When
        final List<Menu> list = menuService.list();

        // Then
        verify(menuDao).findAll();
        assertThat(list).hasSize(1);
    }
}
