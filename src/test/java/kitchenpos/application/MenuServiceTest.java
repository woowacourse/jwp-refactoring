package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
class MenuServiceTest {

    private static final String MENU_NAME = "메뉴";

    private final MenuService menuService;
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    @Autowired
    public MenuServiceTest(final MenuService menuService, final MenuDao menuDao, final MenuGroupDao menuGroupDao,
                           final MenuProductDao menuProductDao, final ProductDao productDao) {
        this.menuService = menuService;
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    @Test
    void create() {
        final var menuGroup = menuGroupDao.save(new MenuGroup("중식"));
        final var product = productDao.save(new Product("자장면", 5000));
        final var menuProduct = menuProductDao.save(new MenuProduct(1L, product.getId(), 1));

        final Menu expected = new Menu("자장면", product.getPrice(), menuGroup.getId(), menuProduct);
        final Menu actual = menuDao.save(expected);
        actual.setMenuProducts(menuProductDao.findAllByMenuId(actual.getId()));

        assertThat(actual.getId()).isPositive();
        assertMenuEqualsWithoutId(actual, expected);
    }

    private void assertMenuEqualsWithoutId(final Menu actual, final Menu expected) {
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getPrice()).isEqualByComparingTo(expected.getPrice());
        assertThat(actual.getMenuGroupId()).isEqualTo(expected.getMenuGroupId());
        assertMenuProductsEquals(actual.getMenuProducts(), expected.getMenuProducts());
    }

    @Test
    void createWithNegativePrice() {
        final var negativePrice = -1;

        final var menu = new Menu(MENU_NAME, negativePrice, 1);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 가격은 양수여야 합니다.");
    }

    @Test
    void createWithNonExistMenuGroup() {
        final var nonExistMenuGroupId = 0L;

        final var menuProduct = new MenuProduct(1, 1, 1);
        final var menu = new Menu(MENU_NAME, 10, nonExistMenuGroupId, menuProduct);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹을 찾을 수 없습니다.");
    }

    @Test
    void createWithNonExistProduct() {
        final var nonExistProductId = 0L;

        final var menuGroup = menuGroupDao.save(new MenuGroup("중식"));
        final var menuProduct = new MenuProduct(1, nonExistProductId, 1);
        final var menu = new Menu(MENU_NAME, 10, menuGroup.getId(), menuProduct);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품을 찾을 수 없습니다.");
    }

    @Test
    void createWithBiggerPriceThenSum() {
        final var menuPrice = 11;
        final var productPrice = 10;

        final var menuGroup = menuGroupDao.save(new MenuGroup("중식"));
        final var product = productDao.save(new Product("상품", productPrice));
        final var menuProduct = new MenuProduct(1, product.getId(), 1);
        final var menu = new Menu(MENU_NAME, menuPrice, menuGroup.getId(), menuProduct);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 가격은 상품 금액 합산보다 클 수 없습니다.");
    }

    @Test
    void list() {
        final List<Menu> expected = List.of(
                saveMenu("중식 세트", 23000, 1,
                        List.of(saveMenuProduct(1, 1, 1),
                                saveMenuProduct(1, 3, 4))),
                saveMenu("일식 세트", 20000, 2,
                        List.of(saveMenuProduct(2, 1, 1),
                                saveMenuProduct(2, 3, 4))));
        final List<Menu> actual = menuService.list();

        assertAllMatches(actual, expected);
    }

    private void assertAllMatches(final List<Menu> actualList, final List<Menu> expectedList) {
        final var expectedSize = actualList.size();
        assertThat(expectedList).hasSize(expectedSize);

        for (int i = 0; i < expectedSize; i++) {
            final var actual = actualList.get(i);
            final var expected = expectedList.get(i);

            assertThat(actual).usingRecursiveComparison()
                    .ignoringFields("menuProducts")
                    .isEqualTo(expected);
            assertMenuProductsEquals(actual.getMenuProducts(), expected.getMenuProducts());
        }
    }

    private void assertMenuProductsEquals(final List<MenuProduct> actualList, final List<MenuProduct> expectedList) {
        final var expectedSize = actualList.size();
        assertThat(expectedList).hasSize(expectedSize);

        for (int i = 0; i < expectedSize; i++) {
            final var actual = actualList.get(i);
            final var expected = expectedList.get(i);

            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }

    private Menu saveMenu(final String name, final int price, final long menuGroupId,
                          final List<MenuProduct> savedMenuProducts) {
        final var menu = menuDao.save(
                new Menu(name, price, menuGroupId, savedMenuProducts));

        final var menuId = menu.getId();
        final var menuProducts = menuProductDao.findAllByMenuId(menuId);
        menu.setMenuProducts(menuProducts);

        return menu;
    }

    private MenuProduct saveMenuProduct(final long menuId, final long productId, final long quantity) {
        return menuProductDao.save(new MenuProduct(menuId, productId, quantity));
    }
}