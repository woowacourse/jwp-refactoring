package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest {

    @Autowired
    MenuService menuService;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuGroupDao menuGroupDao;

    @Autowired
    MenuProductDao menuProductDao;

    @Autowired
    ProductRepository productDao;

    private MenuGroup menuGroup;
    private List<Product> products;
    private Menu menu;
    private List<MenuProduct> menuProducts;

    @BeforeEach
    void setUp() {
        menuGroup = menuGroupDao.save(MenuGroupFixture.메뉴_그룹_생성());
        products = productDao.saveAll(ProductFixture.상품들_생성(2));
        menu = menuRepository.save(MenuFixture.메뉴_생성(menuGroup, products));
        final MenuProduct menuProduct1 = menuProductDao.save(MenuProductFixture.메뉴_상품_생성(products.get(0), menu));
        final MenuProduct menuProduct2 = menuProductDao.save(MenuProductFixture.메뉴_상품_생성(products.get(1), menu));
        menuProducts = List.of(menuProduct1, menuProduct2);
    }

    @Test
    void 메뉴를_등록한다() {
        // given
        final Menu menu = MenuFixture.메뉴_생성(menuGroup, products);

        // when
        final Menu actual = menuService.create(menu);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual).usingRecursiveComparison()
                          .ignoringFields("id")
                          .isEqualTo(menu);
        });
    }

    @ParameterizedTest
    @NullSource
    void 가격이_null이라면_예외를_반환한다(BigDecimal price) {
        // given
        final Menu menu = new Menu(MenuFixture.메뉴명, price, menuGroup, menuProducts);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 가격이_음수라면_예외를_반환한다() {
        // given
        final BigDecimal negative_price = BigDecimal.valueOf(-10_000);
        final Menu menu = new Menu(MenuFixture.메뉴명, negative_price, menuGroup, menuProducts);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_메뉴_그룹이라면_예외를_반환한다() {
        // given
        final MenuGroup unsavedMenuGroup = MenuGroupFixture.메뉴_그룹_생성();
        final Menu menu = new Menu(MenuFixture.메뉴명, BigDecimal.valueOf(10_000), unsavedMenuGroup, menuProducts);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_상품이라면_예외를_반환한다() {
        // given
        final List<MenuProduct> unsavedMenuProducts = MenuProductFixture.메뉴_상품들_생성(products, menu);
        final Menu menu = new Menu(MenuFixture.메뉴명, BigDecimal.valueOf(10_000), menuGroup, unsavedMenuProducts);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품들의_값보다_메뉴의_가격이_더_크다면_예외를_반환한다() {
        // given
        final BigDecimal priceMoreThanProductsPrice = BigDecimal.valueOf(Integer.MAX_VALUE);
        final Menu menu = new Menu(MenuFixture.메뉴명, priceMoreThanProductsPrice, menuGroup, menuProducts);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_목록을_조회한다() {
        // given
        final List<Menu> menus = MenuFixture.메뉴들_생성(3, menuGroup, products);

        // when
        final List<Menu> actual = menuService.list();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0)).usingRecursiveComparison()
                          .isEqualTo(menus.get(0));
            softAssertions.assertThat(actual.get(1)).usingRecursiveComparison()
                          .isEqualTo(menus.get(1));
            softAssertions.assertThat(actual.get(2)).usingRecursiveComparison()
                          .isEqualTo(menus.get(2));
        });
    }
}
