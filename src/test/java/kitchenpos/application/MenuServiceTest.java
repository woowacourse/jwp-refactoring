package kitchenpos.application;

import static kitchenpos.util.ObjectUtil.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
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
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("메뉴 생성")
    void create() {
        MenuProduct menuProduct = createMenuProduct(null, null, 1L, 10);
        Menu menu = createMenu(1L, null, 1000, 1L, Collections.singletonList(menuProduct));
        Product product = createProduct(null, null, 1000);

        given(menuGroupDao.existsById(1L)).willReturn(true);
        given(productDao.findById(1L)).willReturn(Optional.of(product));
        given(menuDao.save(any(Menu.class))).willReturn(menu);
        given(menuProductDao.save(any(MenuProduct.class))).willReturn(menuProduct);

        Menu result = menuService.create(menu);

        assertThat(result).isNotNull();
        assertAll(
            () -> assertThat(result.getId()).isEqualTo(1L),
            () -> assertThat(result.getMenuGroupId()).isEqualTo(1L),
            () -> assertThat(result.getMenuProducts()).hasSize(1)
        );
    }

    @Test
    @DisplayName("메뉴 목록 조회")
    void list() {
        MenuProduct menuProduct = createMenuProduct(null, 1L, 1L, 10);
        Menu menu = createMenu(1L, null, 1000, 1L, Collections.singletonList(menuProduct));

        given(menuDao.findAll()).willReturn(Collections.singletonList(menu));
        given(menuProductDao.findAllByMenuId(1L)).willReturn(Collections.singletonList(menuProduct));

        List<Menu> result = menuService.list();

        assertThat(result).isNotNull();
        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result.get(0)).usingRecursiveComparison()
                .isEqualTo(menu)
        );
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(ints = {-10})
    @DisplayName("가격이 null이거나 0미만인 경우 예외 발생")
    void invalidPrice(Integer input) {
        Menu menu = createMenu(null, null, input, null, Collections.emptyList());
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴에 속한 상품 금액의 합이 메뉴의 가격보다 작은 경우 예외 발생")
    void sumOfMenuProductPrice() {
        MenuProduct menuProduct = createMenuProduct(null, null, 1L, 10);
        Menu menu = createMenu(1L, null, 1000, 1L, Collections.singletonList(menuProduct));
        Product product = createProduct(null, null, 10);

        given(menuGroupDao.existsById(1L)).willReturn(true);
        given(productDao.findById(1L)).willReturn(Optional.of(product));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 그룹에 존재하지 않는 경우 예외 발생")
    void doesNotExistInMenuGroup() {
        Menu menu = createMenu(null, null, 1000, null, Collections.emptyList());

        given(menuGroupDao.existsById(any())).willReturn(false);

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴에 상품이 존재하지 않는 경우 예외 발생")
    void productDoesNotExist() {
        MenuProduct menuProduct = createMenuProduct(null, null, null, 0);
        Menu menu = createMenu(1L, null, 1000, 1L, Collections.singletonList(menuProduct));

        given(menuGroupDao.existsById(1L)).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
