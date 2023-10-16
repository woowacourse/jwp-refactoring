package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuProductDao menuProductDao;

    private Menu menu;

    @BeforeEach
    void setUp() {
        Product product = productDao.save(ProductFixture.상품_생성("아메리카노", BigDecimal.valueOf(5600)));
        MenuProduct menuProduct = menuProductDao.save(MenuProductFixture.메뉴_재고(1L, product.getId(), 3));
        MenuGroup menuGroup = menuGroupDao.save(MenuGroupFixture.메뉴그룹_생성("음료"));
        menu = MenuFixture.메뉴_생성("아메리카노", BigDecimal.valueOf(5600), menuGroup.getId(), List.of(menuProduct));
    }

    @Test
    void 메뉴를_등록한다() {
        // when
        Menu savedMenu = menuService.create(menu);

        // then
        assertThat(savedMenu).usingRecursiveComparison()
                .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                .ignoringFields("id", "menuProducts.seq")
                .isEqualTo(menu);
    }

    @Test
    void 메뉴_가격이_null이면_등록할_수_없다() {
        // given
        menu.setPrice(null);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menu));
    }

    @Test
    void 메뉴_가격이_0보다_작으면_등록할_수_없다() {
        // given
        menu.setPrice(BigDecimal.valueOf(-1));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menu));
    }

    @Test
    void 메뉴_그룹이_존재하지_않으면_등록할_수_없다() {
        // given
        menu.setMenuGroupId(100L);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menu));
    }

    @Test
    void 메뉴_가격이_재고_가격보다_크면_등록할_수_없다() {
        // given
        menu.setPrice(BigDecimal.valueOf(18000));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menu));
    }

    @Test
    void 메뉴_목록을_조회한다() {
        // given
        Menu savedMenu = menuService.create(menu);

        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus.get(menus.size() - 1))
                .usingRecursiveComparison()
                .ignoringFields("id", "menuProducts.seq")
                .isEqualTo(savedMenu);
    }
}
