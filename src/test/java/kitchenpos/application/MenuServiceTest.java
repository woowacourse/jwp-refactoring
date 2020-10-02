package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private ProductDao productDao;

    @Mock
    private MenuProductDao menuProductDao;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("create: 메뉴 생성 테스트")
    @Test
    void createTest() {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2L);
        final Menu menu = new Menu();
        menu.setName("후라이드");
        menu.setPrice(BigDecimal.valueOf(16000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        final Product product = new Product();
        product.setName("후라이드");
        product.setPrice(BigDecimal.valueOf(16000));

        when(menuDao.save(any())).thenReturn(menu);
        when(menuGroupDao.existsById(anyLong())).thenReturn(true);
        when(productDao.findById(anyLong())).thenReturn(Optional.of(product));
        when(menuProductDao.save(any())).thenReturn(menuProduct);

        final Menu actual = menuService.create(menu);

        assertThat(actual.getName()).isEqualTo(menu.getName());
        assertThat(actual.getPrice()).isEqualTo(menu.getPrice());
    }

    @DisplayName("list: 전체 메뉴 그룹 목록 조회 테스트")
    @Test
    void listTest() {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2L);
        final Menu menu = new Menu();
        menu.setName("후라이드");
        menu.setPrice(BigDecimal.valueOf(16000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        when(menuDao.findAll()).thenReturn(Collections.singletonList(menu));

        assertThat(menuService.list()).hasSize(1);
    }
}
