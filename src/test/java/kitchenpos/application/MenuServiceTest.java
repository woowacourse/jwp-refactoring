package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("Menu Service 테스트")
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @DisplayName("Menu를 생성할 때")
    @Nested
    class CreateMenu {

        @DisplayName("Menu의 Price가 Null이면 예외가 발생한다.")
        @Test
        void priceNullException() {
            // given
            MenuGroup menuGroup = menuGroupDao.save(MenuGroup을_생성한다("엄청난 그룹"));
            Product 치즈버거 = productDao.save(Product를_생성한다("치즈버거", 4_000));
            Product 콜라 = productDao.save(Product를_생성한다("치즈버거", 1_600));
            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거.getId(), 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라.getId(), 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            Menu menu = Menu를_생성한다("엄청난 메뉴", null, menuGroup.getId(), menuProducts);

            // when, then
            assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Menu의 Price가 0보다 작으면 예외가 발생한다.")
        @Test
        void priceNegativeException() {
            // given
            MenuGroup menuGroup = menuGroupDao.save(MenuGroup을_생성한다("엄청난 그룹"));
            Product 치즈버거 = productDao.save(Product를_생성한다("치즈버거", 4_000));
            Product 콜라 = productDao.save(Product를_생성한다("치즈버거", 1_600));
            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거.getId(), 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라.getId(), 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            Menu menu = Menu를_생성한다("엄청난 메뉴", -1, menuGroup.getId(), menuProducts);

            // when, then
            assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Menu의 MenuGroupId가 존재하지 않는 경우 발생한다.")
        @Test
        void noExistMenuGroupIdException() {
            // given
            Product 치즈버거 = productDao.save(Product를_생성한다("치즈버거", 4_000));
            Product 콜라 = productDao.save(Product를_생성한다("치즈버거", 1_600));
            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거.getId(), 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라.getId(), 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            Menu menu = Menu를_생성한다("엄청난 메뉴", 5_600, -1L, menuProducts);

            // when, then
            assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Menu의 Product가 실제로 존재하지 않는 경우 예외가 발생한다.")
        @Test
        void noExistProductException() {
            // given
            MenuGroup menuGroup = menuGroupDao.save(MenuGroup을_생성한다("엄청난 그룹"));
            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(-1L, 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(-2L, 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            Menu menu = Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts);

            // when, then
            assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Menu의 총 Price가 Product들의 Price 합보다 클 경우 예외가 발생한다.")
        @Test
        void menuPriceNotMatchException() {
            // given
            MenuGroup menuGroup = menuGroupDao.save(MenuGroup을_생성한다("엄청난 그룹"));
            Product 치즈버거 = productDao.save(Product를_생성한다("치즈버거", 4_000));
            Product 콜라 = productDao.save(Product를_생성한다("치즈버거", 1_600));
            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거.getId(), 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라.getId(), 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            Menu menu = Menu를_생성한다("엄청난 메뉴", 5_601, menuGroup.getId(), menuProducts);

            // when, then
            assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("정상적인 경우 menuProduct가 함께 저장되어 반환된다.")
        @Test
        void success() {
            // given
            MenuGroup menuGroup = menuGroupDao.save(MenuGroup을_생성한다("엄청난 그룹"));
            Product 치즈버거 = productDao.save(Product를_생성한다("치즈버거", 4_000));
            Product 콜라 = productDao.save(Product를_생성한다("치즈버거", 1_600));
            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거.getId(), 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라.getId(), 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            Menu menu = Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts);

            // when
            Menu savedMenu = menuService.create(menu);

            // then
            assertThat(savedMenu.getId()).isNotNull();
            assertThat(savedMenu.getName()).isEqualTo(menu.getName());
            assertThat(savedMenu.getPrice().compareTo(menu.getPrice())).isEqualTo(0);
            assertThat(savedMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
            for (MenuProduct menuProduct : savedMenu.getMenuProducts()) {
                assertThat(menuProduct.getSeq()).isNotNull();
                assertThat(menuProduct.getMenuId()).isEqualTo(savedMenu.getId());
            }
        }
    }

    @DisplayName("Menu 목록을 조회할 때 Menu마다의 MenuProduct도 함께 조회된다.")
    @Test
    void list() {
        // given
        List<Menu> beforeSavedMenus = menuService.list();

        MenuGroup menuGroup = menuGroupDao.save(MenuGroup을_생성한다("엄청난 그룹"));
        Product 치즈버거 = productDao.save(Product를_생성한다("치즈버거", 4_000));
        Product 콜라 = productDao.save(Product를_생성한다("치즈버거", 1_600));
        MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거.getId(), 1);
        MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라.getId(), 1);
        List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

        beforeSavedMenus.add(menuService.create(Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts)));
        beforeSavedMenus.add(menuService.create(Menu를_생성한다("할인 메뉴", 4_600, menuGroup.getId(), menuProducts)));

        // when
        List<Menu> afterSavedMenus = menuService.list();

        // then
        assertThat(afterSavedMenus).hasSize(beforeSavedMenus.size());
        assertThat(afterSavedMenus).usingRecursiveComparison().isEqualTo(beforeSavedMenus);
        for (Menu afterSavedMenu : afterSavedMenus) {
            assertThat(afterSavedMenu.getMenuProducts()).isNotEmpty();
        }
    }

    private Menu Menu를_생성한다(String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return Menu를_생성한다(name, BigDecimal.valueOf(price), menuGroupId, menuProducts);
    }

    private Menu Menu를_생성한다(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);

        return menu;
    }

    private MenuGroup MenuGroup을_생성한다(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);

        return menuGroup;
    }

    private MenuProduct MenuProduct를_생성한다(Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    private Product Product를_생성한다(String name, int price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));

        return product;
    }
}