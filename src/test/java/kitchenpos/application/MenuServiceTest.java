package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import kitchenpos.utils.TestFixture;

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
        final MenuProduct menuProduct = TestFixture.getMenuProduct(1L);
        final Menu menu = TestFixture.getMenu(menuProduct, 1L, 16000);
        final Product product = TestFixture.getProduct(16000);

        when(menuDao.save(any())).thenReturn(menu);
        when(menuGroupDao.existsById(anyLong())).thenReturn(true);
        when(productDao.findById(anyLong())).thenReturn(Optional.of(product));
        when(menuProductDao.save(any())).thenReturn(menuProduct);

        final Menu actual = menuService.create(menu);

        assertThat(actual.getName()).isEqualTo(menu.getName());
        assertThat(actual.getPrice()).isEqualTo(menu.getPrice());
    }

    @DisplayName("create: 메뉴의 가격이 0원 미만일때 예외 처리")
    @Test
    void createTestByPriceSmallThanZero() {
        final MenuProduct menuProduct = TestFixture.getMenuProduct(1L);
        final Menu menu = TestFixture.getMenu(menuProduct, 1L, -1);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 해당 메뉴 그룹이 DB에 없는 메뉴 그룹일 경우 예외처리")
    @Test
    void createTestByMenuGroupNotExist() {
        final MenuProduct menuProduct = TestFixture.getMenuProduct(1L);
        final Menu menu = TestFixture.getMenu(menuProduct, 1L, 16000);

        when(menuGroupDao.existsById(anyLong())).thenReturn(false);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 메뉴상품의 총합이 0보다 작으면 예외처리")
    @Test
    void createTestByProductPriceSmallThanZero() {
        final MenuProduct menuProduct = TestFixture.getMenuProduct(1L);
        final Menu menu = TestFixture.getMenu(menuProduct, 1L, 16000);
        final Product product = TestFixture.getProduct(-1);

        when(menuGroupDao.existsById(anyLong())).thenReturn(true);
        when(productDao.findById(anyLong())).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("list: 전체 메뉴 그룹 목록 조회 테스트")
    @Test
    void listTest() {
        final MenuProduct menuProduct = TestFixture.getMenuProduct(1L);
        final Menu menu = TestFixture.getMenu(menuProduct, 1L, 16000);

        when(menuDao.findAll()).thenReturn(Collections.singletonList(menu));

        assertThat(menuService.list()).hasSize(1);
    }
}
