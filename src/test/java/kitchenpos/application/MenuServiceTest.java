package kitchenpos.application;

import static kitchenpos.application.fixture.MenuFixtures.*;
import static kitchenpos.application.fixture.MenuGroupFixtures.*;
import static kitchenpos.application.fixture.MenuProductFixtures.*;
import static kitchenpos.application.fixture.ProductFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.FakeMenuDao;
import kitchenpos.dao.FakeMenuGroupDao;
import kitchenpos.dao.FakeMenuProductDao;
import kitchenpos.dao.FakeProductDao;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuServiceTest {

    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;
    private final MenuService menuService;

    @Autowired
    public MenuServiceTest() {
        this.menuDao = new FakeMenuDao();
        this.menuGroupDao = new FakeMenuGroupDao();
        this.menuProductDao = new FakeMenuProductDao();
        this.productDao = new FakeProductDao();
        this.menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }

    @BeforeEach
    void setUp() {
        FakeMenuDao.deleteAll();
        FakeMenuGroupDao.deleteAll();
        FakeMenuProductDao.deleteAll();
        FakeProductDao.deleteAll();
    }

    @Test
    void menu를_생성한다() {
        MenuGroup 한마리메뉴 = menuGroupDao.save(generateMenuGroup("한마리메뉴"));
        Product 후라이드 = productDao.save(generateProduct("후라이드"));

        List<MenuProduct> menuProducts = List.of(generateMenuProduct(후라이드.getId(), 1));
        Menu menu = generateMenu("후라이드치킨", BigDecimal.valueOf(16000), 한마리메뉴.getId(), menuProducts);

        Menu actual = menuService.create(menu);

        assertAll(() -> {
            assertThat(actual.getName()).isEqualTo(menu.getName());
            assertThat(actual.getPrice().compareTo(menu.getPrice())).isEqualTo(0);
            assertThat(actual.getMenuGroupId()).isEqualTo(한마리메뉴.getId());
            assertThat(actual.getMenuProducts()).hasSize(1);
        });
    }

    @Test
    void price가_null인_경우_예외를_던진다() {
        Menu menu = generateMenu("후라이드치킨", null, 1L);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "price가 {0}미만인 경우 예외를 던진다")
    @ValueSource(ints = {-15000, -10, Integer.MIN_VALUE})
    void price가_0미만인_경우_예외를_던진다(final int price) {
        Menu menu = generateMenu("후라이드치킨", BigDecimal.valueOf(price), 1L);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_menuGroupId인_경우_예외를_던진다() {
        Menu menu = generateMenu("후라이드치킨", BigDecimal.valueOf(16000), 0L);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void price가_menu에_속한_product의_총_price보다_큰_경우_예외를_던진다() {
        MenuGroup 한마리메뉴 = menuGroupDao.save(generateMenuGroup("한마리메뉴"));
        Product 후라이드 = productDao.save(generateProduct("후라이드", BigDecimal.valueOf(16000)));

        List<MenuProduct> menuProducts = List.of(generateMenuProduct(후라이드.getId(), 1));
        Menu menu = generateMenu("후라이드치킨", BigDecimal.valueOf(17000), 한마리메뉴.getId(), menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void menu_list를_조회한다() {
        menuDao.save(generateMenu("후라이드치킨", BigDecimal.valueOf(16000), 1L));
        menuDao.save(generateMenu("양념치킨", BigDecimal.valueOf(17000), 1L));

        List<Menu> actual = menuService.list();

        assertThat(actual).hasSize(2);
    }
}
