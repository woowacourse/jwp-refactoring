package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.fixtures.MenuFixtures;
import kitchenpos.fixtures.ProductFixtures;
import org.junit.jupiter.api.BeforeEach;
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

    private Menu menu;

    @BeforeEach
    void setUp() {
        menu = MenuFixtures.createMenu();
    }

    @Test
    void 메뉴를_생성한다() {
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.of(ProductFixtures.createProduct()));
        given(menuDao.save(any())).willReturn(menu);
        given(menuProductDao.save(any())).willReturn(MenuFixtures.createMenuProduct());

        assertDoesNotThrow(() -> menuService.create(menu));
        verify(menuProductDao, times(menu.getMenuProducts().size())).save(any());
        verify(menuDao, times(1)).save(any());
    }

    @Test
    void 메뉴_그룹이_존재하지_않을_경우_예외를_반환한다() {
        given(menuGroupDao.existsById(any())).willReturn(false);

        assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
    }

    @Test
    void 생성_시_가격이_음수이면_예외를_반환한다() {
        assertThrows(IllegalArgumentException.class, () -> menuService.create(MenuFixtures.createMenu(-1000)));
    }

    @Test
    void 생성_시_가격이_메뉴_상품들의_가격보다_크면_예외를_반환한다() {
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.of(ProductFixtures.createProduct()));

        assertThrows(IllegalArgumentException.class, () -> menuService.create(MenuFixtures.createMenu(100000)));
    }

    @Test
    void 메뉴_리스트를_반환한다() {
        given(menuDao.findAll()).willReturn(Collections.singletonList(menu));
        given(menuProductDao.findAllByMenuId(any()))
            .willReturn(Collections.singletonList(MenuFixtures.createMenuProduct()));

        List<Menu> menus = assertDoesNotThrow(() -> menuService.list());
        menus.stream()
            .map(Menu::getMenuProducts)
            .forEach(menuProducts -> assertThat(menuProducts).isNotEmpty());
    }
}
