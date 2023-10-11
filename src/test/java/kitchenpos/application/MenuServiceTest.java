package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Sql(scripts = "classpath:truncate.sql")
@Transactional
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Nested
    @DisplayName("메뉴는 ")
    class Create {

        @Test
        @DisplayName("정상적으로 생성된다.")
        void create() {
            // given
            final Product product = new Product(1L, "후라이드치킨", new BigDecimal("15000.00"));
            productDao.save(product);
            final List<MenuProduct> menuProducts = List.of(new MenuProduct(1L, 1L, 1L, 1));
            menuGroupDao.save(new MenuGroup(1L, "치킨"));
            final Menu menu = new Menu(1L, "후라이드치킨", new BigDecimal("15000.00"), 1L, menuProducts);

            // when
            final Menu savedMenu = menuService.create(menu);

            // then
            assertAll(
                    () -> assertThat(savedMenu.getName()).isEqualTo("후라이드치킨"),
                    () -> assertThat(savedMenu.getPrice()).isEqualTo("15000.00"),
                    () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(1L),
                    () -> assertThat(savedMenu.getMenuProducts()).usingRecursiveComparison().isEqualTo(menuProducts)
            );
        }

        @Test
        @DisplayName("가격이 빈 값이면 예외가 발생한다.")
        void throwsExceptionWhenPriceIsNull() {
            // given
            final Product product = new Product(1L, "후라이드치킨", new BigDecimal("15000.00"));
            productDao.save(product);
            final List<MenuProduct> menuProducts = List.of(new MenuProduct(1L, 1L, 1L, 1));
            menuGroupDao.save(new MenuGroup(1L, "치킨"));
            final Menu menu = new Menu(1L, "후라이드치킨", null, 1L, menuProducts);

            // when, then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {"-1000", "-1", "-99999"})
        @DisplayName("가격이 0보다 작다면 예외가 발생한다.")
        void throwsExceptionWhenPriceIsUnderZero(String price) {
            // given
            final Product product = new Product(1L, "후라이드치킨", new BigDecimal("15000.00"));
            productDao.save(product);
            final List<MenuProduct> menuProducts = List.of(new MenuProduct(1L, 1L, 1L, 1));
            menuGroupDao.save(new MenuGroup(1L, "치킨"));
            final Menu menu = new Menu(1L, "후라이드치킨", new BigDecimal(price), 1L, menuProducts);

            // when, then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴에 해당하는 상품이 존재하지 않는 경우에 예외가 발생한다.")
        void throwsExceptionWhenProductIdNonExist() {
            // given
            final List<MenuProduct> menuProducts = List.of(new MenuProduct(1L, 1L, 1L, 2));
            menuGroupDao.save(new MenuGroup(1L, "치킨"));
            final Menu menu = new Menu(1L, "후라이드치킨", new BigDecimal("31000.00"), 1L, menuProducts);

            // when, then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴 가격이 상품의 금액(가격 * 수량)의 합보다 큰 경우 예외가 발생한다.")
        void throwsExceptionWhenPriceIsUnderZero() {
            // given
            final Product product = new Product(1L, "후라이드치킨", new BigDecimal("15000.00"));
            productDao.save(product);
            final List<MenuProduct> menuProducts = List.of(new MenuProduct(1L, 1L, 1L, 2));
            menuGroupDao.save(new MenuGroup(1L, "치킨"));
            final Menu menu = new Menu(1L, "후라이드치킨", new BigDecimal("31000.00"), 1L, menuProducts);

            // when, then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("메뉴 목록을 정상적으로 조회한다.")
    void list() {
        // given
        final Product productA = new Product(1L, "후라이드치킨", new BigDecimal("15000.00"));
        productDao.save(productA);
        final List<MenuProduct> menuAProducts = List.of(new MenuProduct(1L, 1L, 1L, 1));
        menuGroupDao.save(new MenuGroup(1L, "치킨"));
        final Menu menuA = new Menu(1L, "후라이드치킨", new BigDecimal("15000.00"), 1L, menuAProducts);
        menuDao.save(menuA);

        final Product productB = new Product(2L, "양념치킨", new BigDecimal("17000.00"));
        productDao.save(productB);
        final List<MenuProduct> menuBProducts = List.of(new MenuProduct(2L, 2L, 2L, 1));
        final Menu menuB = new Menu(2L, "양념치킨", new BigDecimal("17000.00"), 1L, menuBProducts);
        menuDao.save(menuB);

        // when
        final List<Menu> menus = menuService.list();

        // then
        final Menu savedMenuA = menus.get(0);
        final Menu savedMenuB = menus.get(1);
        assertAll(
                () -> assertThat(menus).hasSize(2),
                () -> assertThat(savedMenuA.getId()).isEqualTo(1L),
                () -> assertThat(savedMenuA.getName()).isEqualTo("후라이드치킨"),
                () -> assertThat(savedMenuA.getPrice()).isEqualTo("15000.00"),
                () -> assertThat(savedMenuA.getMenuGroupId()).isEqualTo(1L),
                () -> assertThat(savedMenuB.getId()).isEqualTo(2L),
                () -> assertThat(savedMenuB.getName()).isEqualTo("양념치킨"),
                () -> assertThat(savedMenuB.getPrice()).isEqualTo("17000.00"),
                () -> assertThat(savedMenuB.getMenuGroupId()).isEqualTo(1L)
        );
    }
}
