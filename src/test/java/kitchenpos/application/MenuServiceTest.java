package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.application.fixture.MenuFixture;
import kitchenpos.application.fixture.MenuGroupFixture;
import kitchenpos.application.fixture.ProductFixture;
import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.JdbcTemplateMenuProductDao;
import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

class MenuServiceTest extends AbstractServiceTest {
    private MenuService menuService;
    private MenuDao menuDao;
    private MenuGroupDao menuGroupDao;
    private MenuProductDao menuProductDao;
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        menuDao = new JdbcTemplateMenuDao(dataSource);
        menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
        menuProductDao = new JdbcTemplateMenuProductDao(dataSource);
        productDao = new JdbcTemplateProductDao(dataSource);

        menuService = new MenuService(
            menuDao,
            menuGroupDao,
            menuProductDao,
            productDao
        );
    }

    @DisplayName("메뉴의 가격이 없는 경우 예외를 반환한다.")
    @Test
    void menuPriceIsNull() {
        Menu menu = MenuFixture.createWithPrice(null);

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 0보다 작은 경우 예외를 반환한다.")
    @Test
    void menuProductUnder0() {
        Menu menu = MenuFixture.createWithPrice(BigDecimal.valueOf(-1));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴가 특정 메뉴 그룹에 속해있지 않으면 예외를 반환한다.")
    @Test
    void withOutMenuGroupId() {
        Menu menu = MenuFixture.createWithoutMenuGroupId();

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 속해있는 상품의 아이디가 없거나, DB에 없다면 예외를 반환한다.")
    @Test
    void menuWithOutProductId() {
        MenuGroup menuGroup = MenuGroupFixture.createWithoutId();
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        Menu menu = MenuFixture.createWithNotExistProductId(savedMenuGroup.getId());

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 각각의 가격 * 수량의 결과가 Menu객체의 가격보다 작은 경우 예외를 반환한다.")
    @Test
    void totalPriceUnder0() {
        MenuGroup menuGroup = MenuGroupFixture.createWithoutId();
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        List<Product> products = Arrays.asList(
            productDao.save(ProductFixture.createWithOutId(BigDecimal.valueOf(200))),
            productDao.save(ProductFixture.createWithOutId(BigDecimal.valueOf(200))),
            productDao.save(ProductFixture.createWithOutId(BigDecimal.valueOf(200))),
            productDao.save(ProductFixture.createWithOutId(BigDecimal.valueOf(200)))
        );
        Menu menu = MenuFixture.createWithMenuPriceAndProducts(1000, products, savedMenuGroup.getId());

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정상적으로 메뉴가 저장된다.")
    @Test
    void create() {
        Menu savedMenu = createValidMenu();
        Long menuId = savedMenu.getId();

        assertAll(
            () -> assertThat(menuId).isNotNull(),
            () -> assertThat(savedMenu.getMenuProducts())
                .extracting(MenuProduct::getMenuId)
                .allMatch(id -> id.equals(menuId)),
            () -> assertThat(savedMenu.getMenuProducts())
                .usingFieldByFieldElementComparator()
                .containsAll(savedMenu.getMenuProducts())
        );
    }

    @DisplayName("모든 메뉴를 찾아온다.")
    @Test
    void list() {
        Menu menu = createValidMenu();
        Menu menu2 = createValidMenu();
        List<Menu> list = menuService.list();

        assertAll(
            () -> assertThat(list).usingElementComparatorIgnoringFields("price", "menuProducts")
                .containsAll(Arrays.asList(menu, menu2)),
            () -> assertThat(list)
                .extracting(Menu::getPrice)
                .usingComparatorForType(Comparator.comparingLong(BigDecimal::longValue), BigDecimal.class)
                .containsAll(Arrays.asList(menu.getPrice(), menu2.getPrice()))
        );
    }

    private Menu createValidMenu() {
        MenuGroup savedMenuGroup = menuGroupDao.save(MenuGroupFixture.createWithoutId());
        List<Product> products = Arrays.asList(
            productDao.save(ProductFixture.createWithOutId(BigDecimal.valueOf(200))),
            productDao.save(ProductFixture.createWithOutId(BigDecimal.valueOf(200)))
        );
        Menu menu = MenuFixture.createWithMenuPriceAndProducts(
            400,
            savedMenuGroup.getId(),
            products
        );

        return menuService.create(menu);
    }
}