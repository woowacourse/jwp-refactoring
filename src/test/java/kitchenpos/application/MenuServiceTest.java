package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.generator.MenuGenerator;
import kitchenpos.generator.ProductGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class MenuServiceTest extends ServiceTest {

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

    @DisplayName("메뉴 등록")
    @Test
    void create() {
        Menu menuToCreate = MenuGenerator.newInstance("후라이드+후라이드", 19000, 1L, Collections.singletonList(MenuGenerator.newMenuProduct(1L, 2)));
        when(menuDao.save(menuToCreate)).thenAnswer(invocation -> {
            Menu menu = invocation.getArgument(0);
            return MenuGenerator.newInstance(2L, menu.getName(), menu.getPrice().intValue(), menu.getMenuGroupId(), menu.getMenuProducts());
        });
        when(menuProductDao.save(any(MenuProduct.class))).thenAnswer(invocation -> {
            MenuProduct menuProduct = invocation.getArgument(0);
            return MenuGenerator.newMenuProduct(3L, menuProduct.getMenuId(), menuProduct.getProductId(), (int) menuProduct.getQuantity());
        });
        when(menuGroupDao.existsById(menuToCreate.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(1L)).thenAnswer(invocation -> Optional.of(ProductGenerator.newInstance(1L, "후라이드", 16000)));

        Menu actual = menuService.create(menuToCreate);

        Menu expected = new Menu();
        expected.setId(2L);
        expected.setName(menuToCreate.getName());
        expected.setPrice(menuToCreate.getPrice());
        expected.setMenuGroupId(1L);
        expected.setMenuProducts(Collections.singletonList(MenuGenerator.newMenuProduct(3L, 2L, 1L, 2)));
        verify(menuDao, times(1)).save(menuToCreate);
        verify(menuProductDao, times(1)).save(any(MenuProduct.class));
        assertThat(actual).usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @DisplayName("가격이 음수인 메뉴 등록시 예외 처리")
    @Test
    void createWithNegativePrice() {
        Menu menuToCreate = MenuGenerator.newInstance("후라이드+후라이드", -1, 1L, Collections.singletonList(MenuGenerator.newMenuProduct(1L, 2)));

        assertThatThrownBy(() -> menuService.create(menuToCreate)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 메뉴 상품 가격 합보다 크면 예외 처리")
    @Test
    void createWhenPriceOfMenuIsGreaterThanTotalPriceOfMenuProduct() {
        Menu menuToCreate = MenuGenerator.newInstance("후라이드+후라이드", 33000, 1L, Collections.singletonList(MenuGenerator.newMenuProduct(1L, 2)));
        when(menuGroupDao.existsById(menuToCreate.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(1L)).thenAnswer(invocation -> Optional.of(ProductGenerator.newInstance(1L, "후라이드", 16000)));

        assertThatThrownBy(() -> menuService.create(menuToCreate)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 메뉴 그룹으로 메뉴 등록시 예외 처리")
    @Test
    void createWithNotFoundMenuGroup() {
        Menu menuToCreate = MenuGenerator.newInstance("후라이드+후라이드", 19000, 1L, Collections.singletonList(MenuGenerator.newMenuProduct(1L, 2)));
        when(menuGroupDao.existsById(menuToCreate.getMenuGroupId())).thenReturn(false);

        assertThatThrownBy(() -> menuService.create(menuToCreate)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 상품으로 메뉴 등록시 예외 처리")
    @Test
    void createWithNotFoundProduct() {
        Menu menuToCreate = MenuGenerator.newInstance("후라이드+후라이드", 19000, 1L, Collections.singletonList(MenuGenerator.newMenuProduct(1L, 2)));
        when(menuGroupDao.existsById(menuToCreate.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(1L)).thenAnswer(invocation -> Optional.empty());

        assertThatThrownBy(() -> menuService.create(menuToCreate)).isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
