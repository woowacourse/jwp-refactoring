package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.한마리메뉴;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuServiceTest {

    private MenuDao menuDao;
    private MenuGroupDao menuGroupDao;
    private MenuProductDao menuProductDao;
    private ProductDao productDao;
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuDao = MenuFixture.setUp().getInMemoryMenuDao();
        menuGroupDao = MenuGroupFixture.setUp().getInMemoryMenuGroupDao();
        menuProductDao = MenuProductFixture.setUp().getInMemoryMenuProductDao();
        productDao = ProductFixture.setUp().getInMemoryProductDao();
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }

    @Test
    @DisplayName("메뉴를 생성한다.")
    void createMenu() {
        Menu menu = new Menu();
        menu.setName("맵슐랭순살");
        final BigDecimal menuPrice = new BigDecimal(20_000);
        menu.setPrice(menuPrice);
        menu.setMenuGroupId(한마리메뉴);
        final MenuProduct maebsyullaeng = menuProductDao.findById(MenuProductFixture.맵슐랭)
                .orElseThrow();
        menu.setMenuProducts(Collections.singletonList(maebsyullaeng));

        final Menu persistedMenu = menuService.create(menu);

        assertAll(
                () -> assertThat(persistedMenu.getId()).isNotNull(),
                () -> assertThat(persistedMenu.getName()).isEqualTo("맵슐랭순살"),
                () -> assertThat(persistedMenu.getPrice()).isEqualTo(menuPrice)
        );
    }

    @Test
    @DisplayName("메뉴의 가격이 null인 경우 예외 발생")
    void whenMenuPriceIsNull() {
        Menu menu = MenuFixture.createMenuByPrice(null);
        final MenuProduct maebsyullaeng = menuProductDao.findById(MenuProductFixture.맵슐랭)
                .orElseThrow();
        menu.setMenuProducts(Collections.singletonList(maebsyullaeng));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 가격이 음수일 경우 예외 발생")
    void whenMenuPriceIsNegative() {
        Menu menu = MenuFixture.createMenuByPrice(new BigDecimal(-1000));
        final MenuProduct maebsyullaeng = menuProductDao.findById(MenuProductFixture.맵슐랭)
                .orElseThrow();
        menu.setMenuProducts(Collections.singletonList(maebsyullaeng));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 그룹이 올바르지 않은 경우 예외 발생")
    void whenInvalidMenuGroup() {
        long invalidMenuGroupId = 99999L;
        final Menu menu = MenuFixture.createMenu(invalidMenuGroupId);
        final MenuProduct maebsyullaeng = menuProductDao.findById(MenuProductFixture.맵슐랭)
                .orElseThrow();
        menu.setMenuProducts(Collections.singletonList(maebsyullaeng));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품이 올바르지 않은 경우 예외 발생")
    void whenInvalidProduct() {
        long invalidProductId = 99999L;
        final Menu menu = MenuFixture.createMenu();

        menu.setMenuProducts(Collections.singletonList(new MenuProduct(null, invalidProductId, 2)));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 가격이 메뉴의 상품가격의 합보다 비싼 경우 예외 발생")
    void whenMenuProductsIsMoreExpensivePrice() {
        final Menu menu = MenuFixture.createMenuByPrice(new BigDecimal(100_000_000));
        final MenuProduct maebsyullaeng = menuProductDao.findById(MenuProductFixture.맵슐랭)
                .orElseThrow();

        menu.setMenuProducts(Collections.singletonList(maebsyullaeng));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 목록을 가져온다.")
    void getList() {
        final List<Menu> expectedMenus = MenuFixture.setUp()
                .getFixtures();
        final List<Menu> menus = menuService.list();

        assertAll(
                () -> assertThat(menus).hasSameSizeAs(expectedMenus),
                () -> assertThat(menus).usingRecursiveComparison()
                        .isEqualTo(expectedMenus)
        );
    }
}
