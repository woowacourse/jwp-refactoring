package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fakedao.MenuFakeDao;
import kitchenpos.fakedao.MenuGroupFakeDao;
import kitchenpos.fakedao.MenuProductFakeDao;
import kitchenpos.fakedao.ProductFakeDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class MenuServiceTest {

    private MenuDao menuDao = new MenuFakeDao();
    private MenuGroupDao menuGroupDao = new MenuGroupFakeDao();
    private MenuProductDao menuProductDao = new MenuProductFakeDao();
    private ProductDao productDao = new ProductFakeDao();

    private MenuService menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

    @DisplayName("메뉴를 생성할 때")
    @Nested
    class Create {

        private Menu menu;

        @BeforeEach
        void setUp() {
            MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴그룹1"));
            Product product = productDao.save(new Product("상품1", BigDecimal.valueOf(1000)));
            MenuProduct menuProduct = new MenuProduct(product.getId(), 2);
            ArrayList<MenuProduct> menuProducts = new ArrayList<>();
            menuProducts.add(menuProduct);
            menu = new Menu("메뉴1", BigDecimal.valueOf(1000), menuGroup.getId(), menuProducts);
        }

        @DisplayName("성공")
        @Test
        void success() {
            // when
            Menu actual = menuService.create(menu);

            // then
            assertAll(
                    () -> assertThat(menuDao.findById(actual.getId())).isPresent(),
                    () -> assertThat(actual.getPrice()).isEqualTo(BigDecimal.valueOf(1000)),
                    () -> assertThat(actual.getMenuProducts()).hasSize(1),
                    () -> assertThat(actual.getMenuProducts().get(0).getMenuId()).isEqualTo(actual.getId())
            );
        }

        @DisplayName("가격이 0보다 작으면 예외를 발생시킨다.")
        @Test
        void priceLessThanZero_exception() {
            // given
            menu.setPrice(BigDecimal.valueOf(-1));

            // then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("가격이 null이면 예외를 발생시킨다.")
        @Test
        void priceIsNull_exception() {
            // given
            menu.setPrice(null);

            // then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 상품들이 등록되어 있지 않으면 예외를 발생시킨다.")
        @Test
        void notFoundProduct_exception() {
            // given
            MenuProduct menuProduct = new MenuProduct(0L, 2);
            ArrayList<MenuProduct> menuProducts = new ArrayList<>();
            menuProducts.add(menuProduct);
            menu.setMenuProducts(menuProducts);

            // then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("상품들의 (가격 * 개수) 합보다 메뉴의 가격이 크면 예외를 발생시킨다.")
        @Test
        void priceMoreThanSumOfProducts_exception() {
            // given
            menu.setPrice(BigDecimal.valueOf(2100));

            // then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("모든 메뉴를 조회한다.")
    @Test
    void list() {
        // given
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴그룹1"));
        Product product = productDao.save(new Product("상품1", BigDecimal.valueOf(1000)));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 2);
        ArrayList<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(menuProduct);
        Menu menu = new Menu("메뉴1", BigDecimal.valueOf(1000), menuGroup.getId(), menuProducts);
        menuDao.save(menu);

        // when
        List<Menu> actual = menuService.list();

        // then
        assertThat(actual).hasSize(1);
    }
}
