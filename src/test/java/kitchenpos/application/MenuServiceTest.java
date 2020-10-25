package kitchenpos.application;

import static kitchenpos.MenuFixture.*;
import static kitchenpos.ProductFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }

    @DisplayName("id가 없는 메뉴로 id가 있는 메뉴를 정상적으로 생성한다.")
    @Test
    void createTest() {
        final Menu expectedMenu = createMenuWithId(1L);
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(anyLong())).willReturn(Optional.of(createProductWithId(1L)));
        given(menuDao.save(any(Menu.class))).willReturn(createMenuWithId(1L));
        given(menuProductDao.save(any(MenuProduct.class))).willReturn(createMenuProductWithProductId(1L));
        final Menu persistMenu = menuService.create(createMenuWithoutId());

        assertThat(expectedMenu).usingRecursiveComparison().isEqualTo(persistMenu);
    }

    @DisplayName("가격이 없는 메뉴를 생성 시도할 경우 예외를 반환한다.")
    @Test
    void createTest2() {
        final Menu noPriceMenu = createNoPriceMenu();

        assertThatThrownBy(() -> menuService.create(noPriceMenu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 마이너스인 메뉴를 생성 시도할 경우 예외를 반환한다.")
    @Test
    void createTest3() {
        final Menu minusPriceMenu = createMinusPriceMenu();

        assertThatThrownBy(() -> menuService.create(minusPriceMenu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 메뉴 그룹 id로 메뉴 생성 시도할 경우 예외를 반환한다.")
    @Test
    void createTest4() {
        given(menuGroupDao.existsById(anyLong())).willReturn(false);

        assertThatThrownBy(() -> menuService.create(createMenuWithoutId()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 상품 id로 메뉴 생성을 시도할 경우 예외를 반환한다.")
    @Test
    void createTest5() {
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> menuService.create(createMenuWithoutId()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 필요한 상품 가격 합이 메뉴 미만일 경우 예외를 반환한다.")
    @Test
    void createTest6() {
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(anyLong())).willReturn(Optional.of(createProductWithPrice(BigDecimal.valueOf(5000))));

        assertThatThrownBy(() -> menuService.create(createMenuWithoutId()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴들을 정상적으로 조회한다.")
    @Test
    void listTest() {
        final List<Menu> expectedMenus = createMenus();
        given(menuDao.findAll()).willReturn(createMenus());
        given(menuProductDao.findAllByMenuId(anyLong())).willReturn(
            Collections.singletonList(createMenuProductWithProductId(1L)));
        final List<Menu> persistMenus = menuService.list();

        assertThat(expectedMenus).usingRecursiveComparison().isEqualTo(persistMenus);
    }
}
