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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static kitchenpos.fixture.MenuGroupFixture.MENU_GROUP1;
import static kitchenpos.fixture.ProductFixture.PRODUCT_1;
import static kitchenpos.fixture.ProductFixture.PRODUCT_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private ProductDao productDao;

    @Test
    void 메뉴를_생성한다() {
        // given
        MenuGroup menuGroup = menuGroupDao.save(MENU_GROUP1);
        Product product = productDao.save(PRODUCT_1);

        MenuProduct menuProduct = new MenuProduct(null, null, product.getId(), 3);
        Menu menu = new Menu("치킨메뉴", new BigDecimal("30000.00"), menuGroup.getId(), List.of(menuProduct));

        // when
        Menu createdMenu = menuService.create(menu);

        // then
        assertThat(createdMenu).usingRecursiveComparison()
                .ignoringFields("id", "menuProducts.menuId", "menuProducts.seq")
                .isEqualTo(menu);
    }

    @ParameterizedTest
    @MethodSource("menuPriceSource")
    void 메뉴_가격이_음수거나_null이면_예외_발생한다(BigDecimal price) {
        // given
        Menu menu = new Menu("한식", price, 1L,
                List.of(new MenuProduct(null, null, 1L, 3)));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<BigDecimal> menuPriceSource() {
        return Stream.of(
                new BigDecimal(-3),
                null
        );
    }

    @Test
    void 메뉴_프로덕트들의_가격의_합이_메뉴_가격과_같지_않으면_예외_발생한다() {
        // given
        MenuGroup menuGroup = menuGroupDao.save(MENU_GROUP1);
        Product product = productDao.save(PRODUCT_1);

        MenuProduct menuProduct = new MenuProduct(null, null, product.getId(), 2);
        Menu menu = new Menu("치킨메뉴", new BigDecimal("30000.00"), menuGroup.getId(), List.of(menuProduct));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_리스트를_조회한다() {
        // given
        MenuGroup menuGroup1 = menuGroupDao.save(MENU_GROUP1);
        Product product1 = productDao.save(PRODUCT_1);

        MenuProduct menuProduct1 = new MenuProduct(null, null, product1.getId(), 3);
        Menu menu1 = new Menu("치킨메뉴", new BigDecimal("30000.00"), menuGroup1.getId(), List.of(menuProduct1));

        Product product2 = productDao.save(PRODUCT_2);
        MenuProduct menuProduct2 = new MenuProduct(null, null, product2.getId(), 2);
        Menu menu2 = new Menu("치킨메뉴", new BigDecimal("32000.00"), menuGroup1.getId(), List.of(menuProduct2));

        Menu createdMenu1 = menuService.create(menu1);
        Menu createdMenu2 = menuService.create(menu2);

        // given, when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus).usingRecursiveComparison().ignoringFields("id", "menuProducts")
                .isEqualTo(List.of(createdMenu1, createdMenu2));
    }
}
