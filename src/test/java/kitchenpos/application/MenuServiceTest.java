package kitchenpos.application;

import static kitchenpos.util.ObjectUtil.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.*;
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

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuProductRepository menuProductRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("id가 없는 메뉴로 id가 있는 메뉴를 정상적으로 생성한다.")
    @Test
    void createTest() {
        final MenuProduct menuProduct = createMenuProduct(null, 1L, 1L, 10);
        final Menu expectedMenu = createMenu(1L, null, 1000, 1L, Collections.singletonList(menuProduct));
        final Product product = createProduct(1L, null, 1000);

        given(menuGroupRepository.existsById(anyLong())).willReturn(true);
        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));
        given(menuRepository.save(any(Menu.class))).willReturn(expectedMenu);
        given(menuProductRepository.save(any(MenuProduct.class))).willReturn(menuProduct);

        final Menu persistMenu = menuService.create(
            createMenu(null, null, 1000, 1L, Collections.singletonList(menuProduct)));
        assertThat(persistMenu).usingRecursiveComparison().isEqualTo(expectedMenu);
    }

    @DisplayName("가격이 없거나 0 미만인 메뉴를 생성 시도할 경우 예외를 반환한다.")
    @NullSource
    @ValueSource(ints = {-10})
    @ParameterizedTest
    void createTest2(final Integer input) {
        final MenuProduct menuProduct = createMenuProduct(null, 1L, 1L, 10);
        final Menu invalidPriceMenu = createMenu(null, "메뉴", input, 1L, Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(invalidPriceMenu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 메뉴 그룹 id로 메뉴 생성 시도할 경우 예외를 반환한다.")
    @Test
    void createTest3() {
        final MenuProduct menuProduct = createMenuProduct(null, 1L, 1L, 10);
        final Menu menuNotExistMenuGroupId = createMenu(null, "메뉴", 1000, Long.MAX_VALUE,
            Collections.singletonList(menuProduct));

        given(menuGroupRepository.existsById(anyLong())).willReturn(false);

        assertThatThrownBy(() -> menuService.create(menuNotExistMenuGroupId))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 상품 id로 메뉴 생성을 시도할 경우 예외를 반환한다.")
    @Test
    void createTest4() {
        final MenuProduct menuProduct = createMenuProduct(null, 1L, Long.MAX_VALUE, 10);
        final Menu menuNotExistProductId = createMenu(null, "메뉴", 1000, 1L, Collections.singletonList(menuProduct));

        given(menuGroupRepository.existsById(anyLong())).willReturn(true);
        given(productRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> menuService.create(menuNotExistProductId))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 필요한 상품 가격 합이 메뉴 가격 미만일 경우 예외를 반환한다.")
    @Test
    void createTest5() {
        final MenuProduct menuProduct = createMenuProduct(null, 1L, 1L, 10);
        final Menu menu = createMenu(null, "메뉴", 1000, 1L, Collections.singletonList(menuProduct));
        final Product product = createProduct(1L, "후라이드", 10);

        given(menuGroupRepository.existsById(anyLong())).willReturn(true);
        given(productRepository.findById(anyLong())).willReturn(
            Optional.of(product));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴들을 정상적으로 조회한다.")
    @Test
    void listTest() {
        final MenuProduct menuProduct = createMenuProduct(1L, 1L, 1L, 10);
        final List<Menu> expectedMenus = Collections.singletonList(
            createMenu(1L, null, 1000, 1L, Collections.singletonList(menuProduct)));

        given(menuRepository.findAll()).willReturn(expectedMenus);
        given(menuProductRepository.findAllByMenuId(anyLong())).willReturn(Collections.singletonList(menuProduct));

        final List<Menu> persistMenus = menuService.list();
        assertThat(persistMenus).usingRecursiveComparison().isEqualTo(expectedMenus);
    }
}
